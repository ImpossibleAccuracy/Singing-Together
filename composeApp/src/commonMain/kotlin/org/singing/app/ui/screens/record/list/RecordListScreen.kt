package org.singing.app.ui.screens.record.list

import androidx.compose.foundation.ScrollState
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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.singing.app.composeapp.generated.resources.*
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.singing.app.di.module.viewModels
import org.singing.app.domain.model.RecordData
import org.singing.app.domain.model.RecordPoint
import org.singing.app.setup.collectAsStateSafe
import org.singing.app.ui.base.Space
import org.singing.app.ui.common.ContentContainer
import org.singing.app.ui.common.player.RecordPlayer
import org.singing.app.ui.common.player.rememberRecordPlayer
import org.singing.app.ui.screens.record.details.views.RecordDetails
import org.singing.app.ui.screens.record.details.views.RecordDetailsActions
import org.singing.app.ui.screens.record.details.views.RecordDetailsData
import org.singing.app.ui.screens.record.list.views.RecordsListView
import org.singing.app.ui.screens.record.start.SelectRecordTypeScreen
import org.singing.app.ui.views.base.AppFilledButton
import org.singing.app.ui.views.base.list.EmptyView


class RecordListScreen(
    private val defaultSelectedRecord: RecordData? = null,
) : Screen {
    companion object {
        const val PUBLICATION_DESCRIPTION_MAX_LENGTH = 300
    }

    @Composable
    override fun Content() {
        val viewModel = viewModels<RecordListViewModel>()
        val recordPlayer = rememberRecordPlayer()

        val coroutineScope = rememberCoroutineScope()
        val detailsVerticalScroll = rememberScrollState()

        LaunchedEffect(defaultSelectedRecord) {
            if (defaultSelectedRecord != null) {
                viewModel.setSelectedRecord(defaultSelectedRecord)
            }
        }

        val isLoadingRecords by viewModel.isLoadingRecords.collectAsStateSafe()
        val records by viewModel.records.collectAsStateSafe()
        val selectedRecord by viewModel.selectedRecord.collectAsState()

        val currentSelectedRecord = selectedRecord

        ContentContainer {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 12.dp)
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
                if (records.isEmpty() && currentSelectedRecord == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (isLoadingRecords) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary,
                            )
                        } else {
                            NoRecordsContainer()
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
                                selectedRecord = currentSelectedRecord,
                                onSelectedRecordChange = {
                                    if (it != selectedRecord) {
                                        coroutineScope.launch {
                                            recordPlayer.reset()

                                            detailsVerticalScroll.animateScrollTo(0)
                                        }

                                        viewModel.setSelectedRecord(it)
                                    }
                                }
                            )

                        }
                    }

                    if (isLoadingRecords || records.size > 1) {
                        Space(16.dp)
                    }

                    RecordDetailsContainer(
                        modifier = Modifier.weight(1f),
                        scrollState = detailsVerticalScroll,
                        viewModel = viewModel,
                        recordPlayer = recordPlayer,
                        selectedRecord = currentSelectedRecord,
                    )
                }
            }
        }
    }

    @Composable
    private fun NoRecordsContainer() {
        val navigator = LocalNavigator.currentOrThrow

        EmptyView(
            title = stringResource(Res.string.title_empty_records),
            subtitle = stringResource(Res.string.subtitle_empty_records),
            action = {
                AppFilledButton(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    label = stringResource(Res.string.action_create_record),
                    onClick = {
                        navigator.push(SelectRecordTypeScreen())
                    }
                )
            }
        )
    }

    @Composable
    private fun RecordDetailsContainer(
        modifier: Modifier = Modifier,
        scrollState: ScrollState,
        viewModel: RecordListViewModel,
        recordPlayer: RecordPlayer,
        selectedRecord: RecordData?,
    ) {
        if (selectedRecord == null) {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(Res.string.label_select_record),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineSmall,
                )
            }
        } else {
            val coroutineScope = rememberCoroutineScope()

            val user by viewModel.user.collectAsState()
            var recordPoints by remember { mutableStateOf<ImmutableList<RecordPoint>>(persistentListOf()) }

            LaunchedEffect(selectedRecord) {
                recordPoints = viewModel
                    .getRecordPoints(selectedRecord)
                    .toImmutableList()
            }

            RecordDetails(
                modifier = modifier
                    .fillMaxHeight()
                    .verticalScroll(
                        state = scrollState
                    ),
                data = RecordDetailsData(
                    record = selectedRecord,
                    user = user,
                    player = recordPlayer,
                    publication = {
                        coroutineScope.async {
                            viewModel.getRecordPublication(it)
                        }
                    },
                    recordPoints = recordPoints,
                    note = viewModel::getNote,
                ),
                actions = RecordDetailsActions(
                    uploadRecord = viewModel::uploadRecord,
                    publishRecord = viewModel::publishRecord,
                    deleteRecord = viewModel::deleteRecord,
                )
            )
        }
    }

    @Composable
    private fun RecordListContainer(
        modifier: Modifier = Modifier,
        records: ImmutableList<RecordData>,
        selectedRecord: RecordData?,
        onSelectedRecordChange: (RecordData) -> Unit,
    ) {
        RecordsListView(
            modifier = modifier,
            records = records,
            selectedRecord = selectedRecord,
            onSelectedRecordChange = onSelectedRecordChange,
        )
    }
}
