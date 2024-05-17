package org.singing.app.ui.screens.record.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.singing.app.di.module.viewModels
import org.singing.app.domain.model.RecordData
import org.singing.app.setup.collectAsStateSafe
import org.singing.app.ui.base.AppScreen
import org.singing.app.ui.base.Space
import org.singing.app.ui.screens.record.details.views.RecordDetails
import org.singing.app.ui.screens.record.details.views.RecordDetailsActions
import org.singing.app.ui.screens.record.details.views.RecordDetailsData
import org.singing.app.ui.screens.record.list.views.RecordsListView


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

                        RecordDetails(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .verticalScroll(
                                    state = detailsVerticalScroll
                                ),
                            data = RecordDetailsData(
                                record = selectedRecord,
                                user = user,
                                player = viewModel.recordPlayer,
                                publication = viewModel::getRecordPublication,
                                recordPoints = viewModel::getRecordPoints,
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
        RecordsListView(
            modifier = modifier,
            records = records,
            selectedRecord = selectedRecord,
            onSelectedRecordChange = onSelectedRecordChange,
        )
    }
}
