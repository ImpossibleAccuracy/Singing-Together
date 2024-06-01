package com.singing.feature.record

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import app.cash.paging.compose.LazyPagingItems
import cafe.adriel.voyager.navigator.currentOrThrow
import com.singing.app.common.views.base.list.EmptyView
import com.singing.app.common.views.base.list.Loader
import com.singing.app.domain.model.DataState
import com.singing.app.feature.rememberRecordCardActions
import com.singing.app.feature.rememberRecordPlayer
import com.singing.app.navigation.AppNavigator
import com.singing.app.navigation.SharedScreen
import com.singing.app.ui.screen.dimens
import com.singing.domain.model.RecordPoint
import com.singing.feature.record.presenter.generated.resources.*
import com.singing.feature.record.viewmodel.RecordDetailIntent
import com.singing.feature.record.viewmodel.RecordDetailUiState
import com.singing.feature.record.views.RecordDetails
import com.singing.feature.record.views.RecordDetailsActions
import com.singing.feature.record.views.RecordDetailsData
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource


@Composable
fun RecordDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: RecordDetailViewModel,
    uiState: RecordDetailUiState,
    recordPoints: LazyPagingItems<RecordPoint>,
) {
    val navigator = AppNavigator.currentOrThrow
    val player = rememberRecordPlayer()

    val coroutineScope = rememberCoroutineScope()
    val verticalScroll = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(state = verticalScroll)
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen4_5),
    ) {
        when (val state = uiState.record) {
            DataState.Empty -> {
                EmptyView(
                    title = stringResource(Res.string.common_no_data_title),
                    subtitle = stringResource(Res.string.common_no_data_subtitle),
                )
            }

            is DataState.Error -> {
                EmptyView(
                    title = stringResource(Res.string.common_error_title),
                    subtitle = stringResource(Res.string.common_error_subtitle),
                )
            }

            DataState.Loading -> {
                Loader(Modifier.fillMaxSize())
            }

            is DataState.Success -> {
                RecordDetails(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(vertical = MaterialTheme.dimens.dimen2),
                    data = RecordDetailsData(
                        user = uiState.user,
                        record = state.data,
                        player = player,
                        recordPoints = recordPoints,
                        note = viewModel::getNote,
                    ),
                    availableActions = rememberRecordCardActions(uiState.user, state.data),
                    actions = RecordDetailsActions(
                        uploadRecord = {
                            viewModel.onIntent(RecordDetailIntent.UploadRecord)
                        },
                        navigatePublication = {
                            coroutineScope.launch {
                                val publication = viewModel.getRecordPublication()

                                if (publication != null) {
                                    navigator.navigate(
                                        SharedScreen.PublicationDetails(publication)
                                    )
                                }
                            }
                        },
                        publishRecord = {
                            viewModel.onIntent(RecordDetailIntent.PublishRecord(it))
                        },
                        deleteRecord = {
                            viewModel.onIntent(RecordDetailIntent.DeleteRecord)
                        },
                    ),
                )
            }
        }
    }
}
