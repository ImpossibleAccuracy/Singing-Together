package com.singing.app.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.singing.feature.main.MainPage

@Composable
actual fun getStartDestination(): Screen =
    MainPage()
