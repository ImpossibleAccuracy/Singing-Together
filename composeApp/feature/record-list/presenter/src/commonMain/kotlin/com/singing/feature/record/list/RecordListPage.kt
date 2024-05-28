package com.singing.feature.record.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.currentOrThrow
import com.singing.app.domain.model.RecordData
import com.singing.app.navigation.AppNavigator
import com.singing.app.navigation.SharedScreen
import com.singing.app.navigation.base.AppPage
import com.singing.app.navigation.base.extensions.FabScreen
import com.singing.app.navigation.base.screenModel
import com.singing.app.navigation.views.ContentContainer
import com.singing.feature.record.presenter.generated.resources.Res
import com.singing.feature.record.presenter.generated.resources.baseline_mic_24
import org.jetbrains.compose.resources.vectorResource

data class RecordListPage(
    val initialRecord: RecordData?,
) : AppPage<RecordListViewModel>(), FabScreen {
    @Composable
    override fun rememberLocalModelFactory(): RecordListViewModel {
        return screenModel<RecordListViewModel>()
    }

    @Composable
    override fun Content(screenModel: RecordListViewModel) {
        ContentContainer {
            val uiState by screenModel.uiState.collectAsState()

            RecordListScreen(
                modifier = Modifier.fillMaxSize(),
                viewModel = screenModel,
                uiState = uiState,
            )
        }
    }

    @Composable
    override fun Fab() {
        val navigator = AppNavigator.currentOrThrow

        FloatingActionButton(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
            onClick = {
                navigator.navigate(SharedScreen.SelectRecordType)
            }
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.baseline_mic_24),
                contentDescription = "",
            )
        }
    }
}
