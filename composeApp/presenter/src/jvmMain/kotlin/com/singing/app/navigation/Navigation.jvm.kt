package com.singing.app.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.singing.app.feature.main.MainPage
import com.singing.app.feature.community.CommunityScreen

@Composable
actual fun getStartDestination(): Screen =
    CommunityScreen()

actual fun isRootScreen(screen: Screen): Boolean =
    when (screen) {
        is MainPage -> true
        else -> false
    }
