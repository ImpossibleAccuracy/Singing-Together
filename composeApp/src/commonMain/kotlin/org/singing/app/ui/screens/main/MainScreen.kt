package org.singing.app.ui.screens.main

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.singing.app.di.module.viewModels
import org.singing.app.ui.screens.record.RecordScreen

class MainScreen : Screen, ScreenProvider {
    @Composable
    override fun Content() {
        val mainViewModel = viewModels<MainViewModel>()

        val navigator = LocalNavigator.currentOrThrow

        Button(
            onClick = {
                navigator.push(RecordScreen())
            }
        ) {
            Text("Details")
        }
    }
}
