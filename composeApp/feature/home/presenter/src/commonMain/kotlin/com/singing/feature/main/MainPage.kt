package com.singing.feature.main

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.currentOrThrow
import com.singing.app.navigation.AppNavigator
import com.singing.app.navigation.SharedScreen
import com.singing.app.navigation.base.AppPage
import com.singing.app.navigation.base.extensions.FabScreen
import com.singing.app.navigation.base.screenModel
import com.singing.app.navigation.views.ContentContainer
import com.singing.feature.main.presenter.generated.resources.Res
import com.singing.feature.main.presenter.generated.resources.baseline_mic_24
import org.jetbrains.compose.resources.vectorResource

class MainPage : AppPage<MainViewModel>(), FabScreen {
    @Composable
    override fun rememberLocalModelFactory(): MainViewModel {
        return screenModel<MainViewModel>()
    }

    @Composable
    override fun Content(screenModel: MainViewModel) {
        ContentContainer {
            val uiState by screenModel.uiState.collectAsState()

            MainScreen(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 44.dp,
                        bottom = 24.dp,
                        start = 12.dp,
                        end = 12.dp,
                    ),
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
                navigator.navigate(SharedScreen.SelectRecordingType)
            }
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.baseline_mic_24),
                contentDescription = "",
            )
        }
    }
}
