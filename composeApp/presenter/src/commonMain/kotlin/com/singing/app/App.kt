package com.singing.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.singing.app.navigation.AppNavigator
import com.singing.app.navigation.NavigatorImpl
import com.singing.app.navigation.getStartDestination
import com.singing.app.ui.navigation.NavigationAppearance
import com.singing.app.ui.theme.AppTheme


@Composable
internal fun App() {
    AppTheme {
        Navigator(
            screen = getStartDestination(),
            onBackPressed = {
                true
            }
        ) { navigator ->
            val navImpl = remember(navigator) { NavigatorImpl(navigator) }

            CompositionLocalProvider(AppNavigator provides navImpl) {
                NavigationAppearance(
                    navigator = navigator,
                ) {
                    CurrentScreen()
                }
            }
        }
    }
}
