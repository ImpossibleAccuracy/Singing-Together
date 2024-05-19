package org.singing.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.ui.common.navigation.NoNavigationScreen
import org.singing.app.ui.navigation.NavigationItems


@Composable
actual fun NavigationAppearance(
    navigator: Navigator,
    onNavigateUp: (screen: Screen) -> Unit,
    content: @Composable BoxScope.() -> Unit,
) {
    val currentScreen = navigator.lastItemOrNull

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            if (currentScreen !is NoNavigationScreen) {
                NavigationBar {
                    NavigationItems.forEach {
                        val title = stringResource(it.title)
                        val icon = vectorResource(it.icon)

                        NavigationBarItem(
                            selected = if (currentScreen == null) false else it.isInstance(currentScreen),
                            label = { Text(title) },
                            icon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = title,
                                )
                            },
                            onClick = {
                                val screen = it.screenProvider()

                                navigator.push(screen)
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues),
            content = content,
        )
    }
}
