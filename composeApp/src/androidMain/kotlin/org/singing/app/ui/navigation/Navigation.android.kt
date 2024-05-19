package org.singing.app.ui.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import org.singing.app.ui.screens.community.CommunityScreen
import org.singing.app.ui.screens.main.MainScreen
import org.singing.app.ui.screens.record.list.RecordListScreen

@Composable
actual fun getStartDestination(): Screen =
    MainScreen()

actual fun isRootScreen(screen: Screen): Boolean =
    when (screen) {
        is MainScreen, is RecordListScreen, is CommunityScreen -> true
        else -> false
    }
