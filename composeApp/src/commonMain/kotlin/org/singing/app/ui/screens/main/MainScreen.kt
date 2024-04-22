package org.singing.app.ui.screens.main

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.core.screen.Screen
import org.singing.app.di.module.viewModels

class MainScreen : Screen, ScreenProvider {
    @Composable
    override fun Content() {
        val mainViewModel = viewModels<MainViewModel>()
    }
}
