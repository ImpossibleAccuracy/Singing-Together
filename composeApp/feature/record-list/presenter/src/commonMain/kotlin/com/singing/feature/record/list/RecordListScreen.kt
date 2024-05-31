package com.singing.feature.record.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import app.cash.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.navigator.currentOrThrow
import com.singing.app.common.views.base.list.EmptyView
import com.singing.app.common.views.base.list.Loader
import com.singing.app.domain.model.DataState
import com.singing.app.feature.rememberRecordCardActions
import com.singing.app.feature.rememberRecordPlayer
import com.singing.app.navigation.AppNavigator
import com.singing.app.navigation.SharedScreen
import com.singing.app.ui.screen.WindowSize
import com.singing.app.ui.screen.actualScreenSize
import com.singing.app.ui.screen.dimens
import com.singing.feature.list.record.presenter.generated.resources.Res
import com.singing.feature.list.record.presenter.generated.resources.common_error_subtitle
import com.singing.feature.list.record.presenter.generated.resources.common_error_title
import com.singing.feature.record.list.viewmodel.RecordListIntent
import com.singing.feature.record.list.viewmodel.RecordListUiState
import com.singing.feature.record.list.views.RecordsList
import com.singing.feature.record.views.RecordDetails
import com.singing.feature.record.views.RecordDetailsActions
import com.singing.feature.record.views.RecordDetailsData
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource

private val breakpoint = WindowSize.MEDIUM

@Composable
fun RecordListScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    viewModel: RecordListViewModel,
    uiState: RecordListUiState,
) {
    val coroutineScope = rememberCoroutineScope()
    val navigator = AppNavigator.currentOrThrow
    val windowSize = MaterialTheme.actualScreenSize

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen2),
    ) {
        val records = viewModel.records.collectAsLazyPagingItems()

        RecordsList(
            modifier = Modifier.weight(2f),
            contentPadding = contentPadding,
            records = records,
            onSelectedRecordChange = {
                viewModel.onIntent(RecordListIntent.UpdateSelected(it))

                if (windowSize < breakpoint) {
                    navigator.navigate(SharedScreen.RecordDetails(it))
                }
            }
        )

        if (MaterialTheme.actualScreenSize >= breakpoint &&
            (records.itemCount > 0 || uiState.selectedRecord is DataState.Success)
        ) {
            val detailsModifier = Modifier.weight(3f)

            when (val state = uiState.selectedRecord) {
                is DataState.Empty -> {}

                is DataState.Error -> {
                    EmptyView(
                        modifier = detailsModifier,
                        title = stringResource(Res.string.common_error_title),
                        subtitle = stringResource(Res.string.common_error_subtitle),
                    )
                }

                is DataState.Loading -> {
                    Loader(detailsModifier)
                }

                is DataState.Success -> {
                    val player = rememberRecordPlayer()

                    val recordPoints = viewModel.recordPoints.collectAsLazyPagingItems()

                    RecordDetails(
                        modifier = detailsModifier
                            .fillMaxSize()
                            .verticalScroll(state = rememberScrollState())
                            .padding(contentPadding),
                        data = RecordDetailsData(
                            user = uiState.user,
                            record = state.data,
                            player = player,
                            editable = true,
                            recordPoints = recordPoints,
                            isRecordPointsStatic = true,
                            note = viewModel::getNote
                        ),
                        availableActions = rememberRecordCardActions(uiState.user, state.data),
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
                                viewModel.onIntent(
                                    RecordListIntent.PublishRecord(
                                        state.data,
                                        it
                                    )
                                )
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
