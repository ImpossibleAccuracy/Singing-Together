package org.singing.app.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import org.singing.app.ui.common.navigation.NoNavigationScreen
import org.singing.app.ui.views.navigation.AppNavigationRail


@Composable
actual fun NavigationAppearance(
    navigator: Navigator,
    content: @Composable BoxScope.() -> Unit
) {
    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        Row(
            modifier = Modifier.padding(paddingValues),
        ) {
            val currentScreen = navigator.lastItemOrNull

            if (currentScreen !is NoNavigationScreen) {
                AppNavigationRail(
                    currentScreen = navigator.lastItemOrNull,
                    navigate = { _, destination ->
                        navigator.popAll()
                        navigator.push(destination)
                    },
                    onNavigationClick = { screen ->
                        if (isRootScreen(screen)) {
                            navigator.popAll()
                        } else {
                            navigator.pop()
                        }
                    }
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                content = content,
            )
        }
    }
}
