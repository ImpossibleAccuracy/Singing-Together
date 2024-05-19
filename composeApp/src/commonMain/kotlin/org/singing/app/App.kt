package org.singing.app

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import org.singing.app.ui.base.AppScreen
import org.singing.app.ui.navigation.getStartDestination
import org.singing.app.ui.theme.AppTheme

@Composable
expect fun NavigationAppearance(
    navigator: Navigator,
    onNavigateUp: (screen: Screen) -> Unit,
    content: @Composable BoxScope.() -> Unit,
)


@Composable
internal fun App() {
    AppTheme {
        Navigator(
            screen = getStartDestination(),
            onBackPressed = {
                onNavigateUp(it)

                true
            }
        ) { navigator ->
            LaunchedEffect(navigator.lastItem) {
                val prevItem = navigator.items.getOrNull(navigator.items.lastIndex - 1)

                if (prevItem is AppScreen) {
                    // TODO: untested API
                    prevItem.onLeave()
                }
            }

            NavigationAppearance(
                navigator = navigator,
                onNavigateUp = ::onNavigateUp,
            ) {
                CurrentScreen()
            }
        }
    }
}

private fun onNavigateUp(screen: Screen) {
    if (screen is AppScreen) {
        screen.onClose()
    }
}
