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
import kotlinx.coroutines.launch
import org.singing.app.domain.model.AccountUiData
import org.singing.app.domain.model.Publication
import org.singing.app.domain.model.RecordData
import org.singing.app.domain.model.RecordPoint
import org.singing.app.domain.player.RecordPlayer
import org.singing.app.setup.collectAsStateSafe
import org.singing.app.ui.base.Space
import org.singing.app.ui.screens.publication.details.PublicationDetailsScreen
import org.singing.app.ui.views.shared.player.PlayerView
import org.singing.app.ui.views.shared.record.DeleteRecordDialog
import org.singing.app.ui.views.shared.record.PublishRecordDialog
import org.singing.app.ui.views.shared.record.RecordCardActionsCallbacks


data class RecordDetailsData(
    val user: AccountUiData?,
    val record: RecordData,
    val player: RecordPlayer,
    val publication: suspend (RecordData) -> Publication,
    val recordPoints: suspend (RecordData) -> List<RecordPoint>,
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
        RecordDetailsContainer(
            data = data,
            actions = actions,
        )

        Space(24.dp)

        RecordDetailsPlayerContainer(
            data = data,
            actions = actions,
        )

        Space(12.dp)

        RecordDetailsPointsContainer(
            data = data,
        )
    }
}

@Composable
private fun RecordDetailsContainer(
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
                    val publication = data.publication(it)

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

    if (recordToPublish != null) {
        PublishRecordDialog(
            onConfirm = {
                actions.publishRecord(recordToPublish!!, it)

                recordToPublish = null
            },
            onDismiss = {
                recordToPublish = null
            },
        )
    }

    if (recordToDelete != null) {
        DeleteRecordDialog(
            onConfirm = {
                actions.deleteRecord(recordToDelete!!)

                recordToDelete = null
            },
            onDismiss = {
                recordToDelete = null
            },
        )
    }
}

@Composable
private fun RecordDetailsPlayerContainer(
    modifier: Modifier = Modifier,
    data: RecordDetailsData,
    actions: RecordDetailsActions,
) {
    val coroutineScope = rememberCoroutineScope()

    val player = remember { data.player }

    val playerState by player.state.collectAsStateSafe()
    val playerPosition by player.position.collectAsStateSafe()

    PlayerView(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                top = 4.dp,
                end = 16.dp,
                bottom = 12.dp,
            ),
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

@Composable
private fun RecordDetailsPointsContainer(
    modifier: Modifier = Modifier,
    data: RecordDetailsData,
) {
    val points = remember { mutableStateListOf<RecordPoint>() }

    LaunchedEffect(data.record) {
        points.clear()

        points.addAll(
            data.recordPoints(data.record)
        )
    }

    RecordPointsView(
        modifier = modifier,
        points = points,
        isTwoLineRecord = data.record.isTwoLineRecord,
        note = data.note,
    )
}