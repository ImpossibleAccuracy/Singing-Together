package org.singing.app

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.ui.base.Space
import org.singing.app.ui.common.navigation.FabScreen
import org.singing.app.ui.common.navigation.NoNavigationScreen
import org.singing.app.ui.navigation.NavigationItems


@Composable
actual fun NavigationAppearance(
    navigator: Navigator,
    onNavigateUp: (screen: Screen) -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        Row(Modifier.padding(paddingValues)) {
            val currentScreen = navigator.lastItemOrNull

            if (currentScreen !is NoNavigationScreen) {
                NavigationRail(
                    modifier = Modifier.padding(
                        top = 44.dp,
                        bottom = 56.dp,
                    )
                ) {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "",
                        )
                    }

                    if (currentScreen is FabScreen) {
                        currentScreen.Fab()
                    }

                    if (currentScreen is FabScreen) {
                        Space(40.dp)
                    } else {
                        Space(100.dp)
                    }

                    NavigationItems.forEach {
                        val title = stringResource(it.title)
                        val icon = vectorResource(it.icon)

                        NavigationRailItem(
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

                                navigator.popUntilRoot()
                                navigator.replace(screen)
                            }
                        )
                    }
                }
            }

            Space(12.dp)

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                content = content,
            )
        }
    }
}
