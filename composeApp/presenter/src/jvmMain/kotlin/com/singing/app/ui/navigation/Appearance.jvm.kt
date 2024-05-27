package com.singing.app.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import com.singing.app.navigation.base.extensions.FabScreen
import com.singing.app.navigation.base.extensions.NoNavigationScreen
import com.singing.app.navigation.isRootScreen
import com.singing.app.navigation.views.AppNavigationRail


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
                    items = NavigationItems,
                    isSelected = {
                        if (currentScreen == null) {
                            false
                        } else {
                            areScreensSame(currentScreen, it.reference())
                        }
                    },
                    navigate = {
                        navigator.push(it.reference().screen)
                    },
                    action = {
                        IconButton(
                            onClick = {
                                if (currentScreen == null || isRootScreen(currentScreen)) {
                                    navigator.popAll()
                                } else {
                                    navigator.pop()
                                }
                            },
                        ) {
                            if (currentScreen == null || isRootScreen(currentScreen)) {
                                Icon(
                                    imageVector = Icons.Filled.Menu,
                                    contentDescription = "",
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "",
                                )
                            }
                        }
                    },
                    fab = when (currentScreen) {
                        is FabScreen -> {
                            {
                                currentScreen.Fab()
                            }
                        }

                        else -> null
                    },
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
