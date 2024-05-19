package org.singing.app

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import org.singing.app.ui.navigation.NavigationAppearance
import org.singing.app.ui.navigation.getStartDestination
import org.singing.app.ui.theme.AppTheme


@Composable
internal fun App() {
    AppTheme {
        Navigator(
            screen = getStartDestination(),
            onBackPressed = {
                true
            }
        ) { navigator ->
            NavigationAppearance(navigator = navigator) {
                CurrentScreen()
            }
        }
    }
}
