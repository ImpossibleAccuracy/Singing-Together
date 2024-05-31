package com.singing.feature.record.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.singing.app.domain.model.RecordData
import com.singing.app.navigation.base.AppPage
import com.singing.app.navigation.base.screenModel
import com.singing.app.navigation.views.ContentContainer

data class RecordListPage(
    val initialRecord: RecordData?,
) : AppPage<RecordListViewModel>() {
    @Composable
    override fun rememberLocalModelFactory(): RecordListViewModel {
        return screenModel<RecordListViewModel>(initialRecord)
    }

    @Composable
    override fun Content(screenModel: RecordListViewModel) {
        ContentContainer {
            val uiState by screenModel.uiState.collectAsState()

            RecordListScreen(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    horizontal = 12.dp,
                    vertical = 12.dp,
                ),
                viewModel = screenModel,
                uiState = uiState,
            )
        }
    }
}
