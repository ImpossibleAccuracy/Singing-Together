package org.singing.app.ui.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import org.singing.app.ui.screens.main.MainScreen

@Composable
actual fun getStartDestination(): Screen =
    MainScreen()
