package org.singing.app.ui.screens.record.details.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.singing.audio.player.PlayerState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch
import org.singing.app.domain.model.AccountUiData
import org.singing.app.domain.model.Publication
import org.singing.app.domain.model.RecordData
import com.singing.app.domain.model.RecordPoint
import org.singing.app.setup.collectAsStateSafe
import org.singing.app.ui.base.Space
import org.singing.app.ui.common.player.RecordPlayer
import org.singing.app.ui.screens.publication.details.PublicationDetailsScreen
import org.singing.app.ui.views.shared.player.PlayerView
import org.singing.app.ui.views.shared.record.dialog.DeleteRecordDialog
import org.singing.app.ui.views.shared.record.dialog.PublishRecordDialog
import org.singing.app.ui.views.shared.record.RecordCardActionsCallbacks


data class RecordDetailsData(
    val user: AccountUiData?,
    val record: RecordData,
    val player: RecordPlayer,
    val publication: (RecordData) -> Deferred<Publication>,
    val recordPoints: ImmutableList<RecordPoint>,
    val note: (Double) -> String,
)

data class RecordDetailsActions(
    val uploadRecord: (RecordData) -> Unit,
    val publishRecord: (RecordData, String) -> Unit,
    val deleteRecord: (RecordData) -> Unit,
)


@Composable
fun RecordDetails(
    modifier: Modifier = Modifier,
    data: RecordDetailsData,
    actions: RecordDetailsActions,
) {
    Column(
        modifier = modifier.padding(vertical = 16.dp)
    ) {
        RecordDetailsMain(
            data = data,
            actions = actions,
        )

        Space(24.dp)

        RecordDetailsPlayer(
            data = data,
        )

        Space(12.dp)

        RecordPointsView(
            modifier = Modifier.fillMaxWidth(),
            points = data.recordPoints,
            isTwoLineRecord = data.record.isTwoLineRecord,
            note = data.note,
        )
    }
}

@Composable
private fun RecordDetailsMain(
    modifier: Modifier = Modifier,
    data: RecordDetailsData,
    actions: RecordDetailsActions,
) {
    val coroutineScope = rememberCoroutineScope()
    val navigator = LocalNavigator.currentOrThrow

    var recordToPublish by remember { mutableStateOf<RecordData?>(null) }
    var recordToDelete by remember { mutableStateOf<RecordData?>(null) }

    RecordDetailsCard(
        modifier = modifier,
        record = data.record,
        accountData = data.user,
        actions = RecordCardActionsCallbacks(
            onUploadRecord = {
                actions.uploadRecord(it)
            },
            showPublication = {
                coroutineScope.launch {
                    val publication = data.publication(it).await()

                    navigator.push(
                        PublicationDetailsScreen(
                            requestedPublication = publication,
                        )
                    )
                }
            },
            onPublishRecord = {
                recordToPublish = it
            },
            onDeleteRecord = {
                recordToDelete = it
            },
        ),
    )

    PublishDialog(
        record = recordToPublish,
        actions = actions,
        onDismiss = { recordToPublish = null },
    )

    DeleteDialog(
        record = recordToDelete,
        actions = actions,
        onDismiss = { recordToDelete = null }
    )
}

@Composable
private fun PublishDialog(
    record: RecordData?,
    actions: RecordDetailsActions,
    onDismiss: () -> Unit,
) {
    if (record != null) {
        PublishRecordDialog(
            onConfirm = {
                actions.publishRecord(record, it)

                onDismiss()
            },
            onDismiss = onDismiss,
        )
    }
}

@Composable
private fun DeleteDialog(
    record: RecordData?,
    actions: RecordDetailsActions,
    onDismiss: () -> Unit,
) {
    if (record != null) {
        DeleteRecordDialog(
            onConfirm = {
                actions.deleteRecord(record)

                onDismiss()
            },
            onDismiss = onDismiss,
        )
    }
}

@Composable
private fun RecordDetailsPlayer(
    modifier: Modifier = Modifier,
    data: RecordDetailsData,
) {
    val coroutineScope = rememberCoroutineScope()

    val player = remember { data.player }

    val playerState by player.state.collectAsStateSafe()
    val playerPosition by player.position.collectAsStateSafe()

    PlayerView(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.secondary,
        inactiveTrackColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        totalDuration = data.record.duration,
        currentPosition = playerPosition,
        isPlaying = playerState == PlayerState.PLAY,
        onPositionChange = {
            coroutineScope.launch {
                player.setPosition(it)
            }
        },
        onPlay = {
            coroutineScope.launch {
                player.play(data.record)
            }
        },
        onStop = {
            coroutineScope.launch {
                player.stop()
            }
        },
    )
}
