package org.singing.app.ui.screens.main

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.singing.config.track.TrackProperties
import kotlinx.coroutines.flow.map
import org.singing.app.di.module.viewModels
import org.singing.app.setup.collectAsStateSafe
import org.singing.app.setup.file.FilePicker
import org.singing.app.ui.screens.record.RecordScreen
import org.singing.app.ui.screens.record.SelectRecordTypeScreen
import org.singing.app.ui.screens.record.viewmodel.RecordViewModel

class MainScreen : Screen, ScreenProvider {
    @Composable
    override fun Content() {
        val mainViewModel = viewModels<MainViewModel>()
        val recordViewModel = viewModels<RecordViewModel>()

        val isRecording by recordViewModel.uiState
            .map { it.isRecording || it.isPlaying }
            .collectAsStateSafe(false)

        val navigator = LocalNavigator.currentOrThrow

        Button(
            onClick = {
                if (isRecording) {
                    navigator.push(
                        RecordScreen(
                            isNewInstance = false,
                        )
                    )
                } else {
                    navigator.push(SelectRecordTypeScreen())
                }
            }
        ) {
            if (isRecording) {
                Text("Вернуться к записи")
            } else {
                Text("Начать запись")
            }
        }
    }
}
