package org.singing.app.ui.screens.record.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import org.singing.app.domain.model.AccountUiData
import org.singing.app.domain.model.RecordData
import org.singing.app.setup.collectAsStateSafe
import org.singing.app.ui.base.AppScreen
import org.singing.app.ui.base.Space
import org.singing.app.ui.screens.record.list.model.RecordOperationTab
import org.singing.app.ui.screens.record.list.views.RecordDetails
import org.singing.app.ui.screens.record.list.views.RecordOperationsTabs
import org.singing.app.ui.screens.record.list.views.RecordsList


class RecordListScreen(
    private val defaultSelectedRecord: RecordData? = null,
    private val defaultSelectedTab: RecordOperationTab = RecordOperationTab.Record,
) : AppScreen() {
    companion object {
        const val CONTENT_ANIMATION_DURATION = 220
        const val PUBLICATION_DESCRIPTION_MAX_LENGTH = 300
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {
        val viewModel = viewModels<RecordListViewModel>()

        LaunchedEffect(defaultSelectedRecord) {
            if (viewModel.selectedRecord.value == null) {
                viewModel.selectedRecord.value = defaultSelectedRecord
            }
        }

        val detailsVerticalScroll = rememberScrollState()

        val user by viewModel.user.collectAsState()

        val records by viewModel.records.collectAsStateSafe()
        val selectedRecord by viewModel.selectedRecord.collectAsState()

        var detailsPagerState = rememberPagerState(
            initialPage = RecordOperationTab.entries.indexOf(defaultSelectedTab),
            pageCount = {
                RecordOperationTab.entries.size
            }
        )

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
                        start = 12.dp,
                        end = 24.dp,
                    )
            ) {
                if (records.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "No records available",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }
                } else {
                    val record = selectedRecord

                    RecordListContainer(
                        records = records,
                        selectedRecord = record,
                        onSelectedRecordChange = {
                            viewModel.selectedRecord.value = it
                        }
                    )

                    Space(16.dp)

                    if (record == null) {
                        Text(
                            text = "Select records",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    } else {
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
                                record = record,
                            )

                            Space(16.dp)

                            RecordOperationsTabs(
                                viewModel = viewModel,
                                record = record,
                                pagerState = detailsPagerState,
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun RecordDetailsContainer(
        viewModel: RecordListViewModel,
        user: AccountUiData?,
        record: RecordData,
    ) {
        RecordDetails(
            record = record,
            accountData = user,
            onUploadRecord = {
                viewModel.uploadRecord(record)
            },
            onDeleteRecord = {
                viewModel.deleteRecord(record)
            },
        )
    }

    @Composable
    private fun RecordListContainer(
        records: List<RecordData>,
        selectedRecord: RecordData?,
        onSelectedRecordChange: (RecordData) -> Unit,
    ) {
        RecordsList(
            records = records,
            selectedRecord = selectedRecord,
            onSelectedRecordChange = onSelectedRecordChange,
        )
    }
}
