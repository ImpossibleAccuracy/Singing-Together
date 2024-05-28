package com.singing.feature.record

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import app.cash.paging.compose.collectAsLazyPagingItems
import com.singing.app.domain.model.RecordData
import com.singing.app.navigation.base.AppPage
import com.singing.app.navigation.base.screenModel
import com.singing.app.navigation.views.ContentContainer
import com.singing.app.navigation.views.DefaultPagePaddings

class RecordDetailPage(
    val recordData: RecordData,
) : AppPage<RecordDetailViewModel>() {
    @Composable
    override fun rememberLocalModelFactory(): RecordDetailViewModel {
        return screenModel<RecordDetailViewModel>(recordData)
    }

    @Composable
    override fun Content(screenModel: RecordDetailViewModel) {
        ContentContainer {
            val uiState by screenModel.uiState.collectAsState()
            val recordPoints = screenModel.recordPoints.collectAsLazyPagingItems()

            RecordDetailScreen(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(DefaultPagePaddings),
                viewModel = screenModel,
                uiState = uiState,
                recordPoints = recordPoints,
            )
        }
    }
}
