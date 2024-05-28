package com.singing.feature.record.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import app.cash.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.navigator.currentOrThrow
import com.singing.app.common.views.base.list.Loader
import com.singing.app.domain.model.DataState
import com.singing.app.domain.model.valueOrNull
import com.singing.app.feature.rememberRecordPlayer
import com.singing.app.navigation.AppNavigator
import com.singing.app.navigation.SharedScreen
import com.singing.app.ui.screen.WindowSize
import com.singing.app.ui.screen.dimens
import com.singing.app.ui.screen.smallestScreenSize
import com.singing.feature.record.list.viewmodel.RecordListIntent
import com.singing.feature.record.list.viewmodel.RecordListUiState
import com.singing.feature.record.views.RecordDetails
import com.singing.feature.record.views.RecordDetailsActions
import com.singing.feature.record.views.RecordDetailsData
import com.singing.feature.record.list.views.RecordsList
import kotlinx.coroutines.launch


@Composable
fun RecordListScreen(
    modifier: Modifier = Modifier,
    viewModel: RecordListViewModel,
    uiState: RecordListUiState,
) {
    val coroutineScope = rememberCoroutineScope()
    val navigator = AppNavigator.currentOrThrow

    Row(
        modifier = Modifier
            .clip(shape = MaterialTheme.shapes.large)
            .background(color = MaterialTheme.colorScheme.surfaceContainerLow),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen2),
    ) {
        val records = viewModel.records.collectAsLazyPagingItems()

        RecordsList(
            modifier = modifier.weight(1f),
            records = records,
            selectedRecord = uiState.selectedRecord.valueOrNull(),
            onSelectedRecordChange = {
                viewModel.onIntent(RecordListIntent.UpdateSelected(it))
            }
        )

        if (MaterialTheme.smallestScreenSize == WindowSize.EXPANDED &&
            (records.itemCount > 0 || uiState.selectedRecord is DataState.Success)
        ) {
            when (val state = uiState.selectedRecord) {
                DataState.Empty -> TODO()

                is DataState.Error -> TODO()

                DataState.Loading -> {
                    Loader(Modifier.fillMaxSize())
                }

                is DataState.Success -> {
                    val player = rememberRecordPlayer()

                    val recordPoints = viewModel.recordPoints.collectAsLazyPagingItems()

                    RecordDetails(
                        modifier = modifier
                            .weight(1f)
                            .verticalScroll(state = rememberScrollState()),
                        data = RecordDetailsData(
                            user = uiState.user,
                            record = state.data,
                            player = player,
                            editable = true,
                            recordPoints = recordPoints,
                            note = {
                                viewModel.getNote(it)
                            }
                        ),
                        actions = RecordDetailsActions(
                            uploadRecord = {
                                viewModel.onIntent(RecordListIntent.UploadRecord(state.data))
                            },
                            navigatePublication = {
                                coroutineScope.launch {
                                    val publication = viewModel.getRecordPublication(state.data)

                                    if (publication != null) {
                                        navigator.navigate(
                                            SharedScreen.PublicationDetails(publication)
                                        )
                                    }
                                }
                            },
                            publishRecord = {
                                viewModel.onIntent(RecordListIntent.PublishRecord(state.data, it))
                            },
                            deleteRecord = {
                                viewModel.onIntent(RecordListIntent.DeleteRecord(state.data))
                            },
                        ),
                    )
                }
            }
        }
    }
}
