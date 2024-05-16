package org.singing.app.ui.screens.record.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.singing.audio.player.PlayerState
import kotlinx.coroutines.launch
import org.singing.app.di.module.viewModels
import org.singing.app.domain.model.AccountUiData
import org.singing.app.domain.model.RecordData
import org.singing.app.domain.model.RecordPoint
import org.singing.app.setup.collectAsStateSafe
import org.singing.app.ui.base.AppScreen
import org.singing.app.ui.base.Space
import org.singing.app.ui.screens.publication.details.PublicationDetailsScreen
import org.singing.app.ui.screens.record.list.views.RecordDetails
import org.singing.app.ui.screens.record.list.views.RecordPointsView
import org.singing.app.ui.screens.record.list.views.RecordsList
import org.singing.app.ui.views.shared.player.PlayerView
import org.singing.app.ui.views.shared.record.DeleteRecordDialog
import org.singing.app.ui.views.shared.record.PublishRecordDialog
import org.singing.app.ui.views.shared.record.RecordCardActionsCallbacks


class RecordListScreen(
    private val defaultSelectedRecord: RecordData? = null,
) : AppScreen() {
    companion object {
        const val PUBLICATION_DESCRIPTION_MAX_LENGTH = 300
    }

    private var _viewModel: RecordListViewModel? = null

    override fun onLeave() {
        super.onLeave()

        _viewModel?.resetRecordPlayer()
    }

    @Composable
    override fun Content() {
        val viewModel = viewModels<RecordListViewModel>()
        _viewModel = viewModel

        val detailsVerticalScroll = rememberScrollState()

        val isLoadingRecords by viewModel.isLoadingRecords.collectAsStateSafe()
        val records by viewModel.records.collectAsStateSafe()
        val selectedRecordIndex by viewModel.selectedRecord.collectAsState()

        LaunchedEffect(defaultSelectedRecord, records) {
            if (viewModel.selectedRecord.value == -1) {
                if (defaultSelectedRecord == null) {
                    viewModel.selectedRecord.value = 0
                } else {
                    viewModel.selectedRecord.value = records.indexOf(defaultSelectedRecord)
                }
            }
        }

        val selectedRecord = when {
            selectedRecordIndex == -1 || records.isEmpty() -> defaultSelectedRecord
            else -> records[selectedRecordIndex]
        }

        LaunchedEffect(selectedRecord) {
            detailsVerticalScroll.animateScrollTo(0)
        }

        ContentContainer {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = MaterialTheme.shapes.large)
                    .background(color = MaterialTheme.colorScheme.surfaceContainerLow)
                    .padding(
                        start = when (records.isEmpty()) {
                            true -> 24.dp
                            false -> 12.dp
                        },
                        end = 24.dp,
                    )
            ) {
                if (records.isEmpty() && selectedRecord == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (isLoadingRecords) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary,
                            )
                        } else {
                            Text(
                                text = "No records available",
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.headlineSmall,
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .width(300.dp)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (isLoadingRecords) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }

                        if (records.size > 1) {
                            RecordListContainer(
                                modifier = Modifier.fillMaxWidth(),
                                records = records,
                                selectedRecord = selectedRecord,
                                onSelectedRecordChange = {
                                    viewModel.selectedRecord.value = records.indexOf(it)
                                }
                            )

                        }
                    }

                    if (isLoadingRecords || records.size > 1) {
                        Space(16.dp)
                    }

                    if (selectedRecord == null) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "Select record",
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.headlineSmall,
                            )
                        }
                    } else {
                        val user by viewModel.user.collectAsState()

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .verticalScroll(
                                    state = detailsVerticalScroll
                                )
                                .padding(vertical = 16.dp)
                        ) {
                            RecordDetailsContainer(
                                viewModel = viewModel,
                                user = user,
                                record = selectedRecord,
                            )

                            Space(24.dp)

                            RecordDetailsPlayerContainer(
                                viewModel = viewModel,
                                record = selectedRecord,
                            )

                            Space(12.dp)

                            RecordDetailsPointsContainer(
                                viewModel = viewModel,
                                record = selectedRecord,
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun RecordListContainer(
        modifier: Modifier = Modifier,
        records: List<RecordData>,
        selectedRecord: RecordData?,
        onSelectedRecordChange: (RecordData) -> Unit,
    ) {
        RecordsList(
            modifier = modifier,
            records = records,
            selectedRecord = selectedRecord,
            onSelectedRecordChange = onSelectedRecordChange,
        )
    }

    @Composable
    private fun RecordDetailsContainer(
        viewModel: RecordListViewModel,
        user: AccountUiData?,
        record: RecordData,
    ) {
        val coroutineScope = rememberCoroutineScope()
        val navigator = LocalNavigator.currentOrThrow

        var recordToPublish by remember { mutableStateOf<RecordData?>(null) }
        var recordToDelete by remember { mutableStateOf<RecordData?>(null) }

        RecordDetails(
            record = record,
            accountData = user,
            actions = RecordCardActionsCallbacks(
                onUploadRecord = {
                    viewModel.uploadRecord(it)
                },
                showPublication = {
                    coroutineScope.launch {
                        val publication = viewModel.getRecordPublication(it)

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
                    viewModel.publishRecord(recordToPublish!!, it)

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
                    viewModel.deleteRecord(recordToDelete!!)

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
        viewModel: RecordListViewModel,
        record: RecordData,
    ) {
        val coroutineScope = rememberCoroutineScope()

        val player = remember { viewModel.recordPlayer }

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
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.tertiary,
            inactiveTrackColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            totalDuration = record.duration,
            currentPosition = playerPosition,
            isPlaying = playerState == PlayerState.PLAY,
            onPositionChange = {
                coroutineScope.launch {
                    player.setPosition(it)
                }
            },
            onPlay = {
                coroutineScope.launch {
                    player.play(record)
                }
            },
            onStop = {
                coroutineScope.launch {
                    player.stop()
                }
            },
        )

        /*RecordPlayerView(
            modifier = modifier,
            duration = record.duration,
            playerState = playerState,
            playerPosition = playerPosition,
            updatePosition = {
                coroutineScope.launch {
                    player.setPosition(it)
                }
            },
            startPlay = {
                coroutineScope.launch {
                    player.play(record)
                }
            },
            stopPlay = {
                coroutineScope.launch {
                    player.stop()
                }
            },
        )*/
    }

    @Composable
    private fun RecordDetailsPointsContainer(
        modifier: Modifier = Modifier,
        viewModel: RecordListViewModel,
        record: RecordData,
    ) {
        val points = remember { mutableStateListOf<RecordPoint>() }

        LaunchedEffect(record) {
            points.clear()

            points.addAll(
                viewModel.getRecordPoints(record)
            )
        }

        RecordPointsView(
            modifier = modifier,
            points = points,
            isTwoLineRecord = record is RecordData.Cover,
            note = {
                viewModel.getNote(it)
            },
        )
    }
}
