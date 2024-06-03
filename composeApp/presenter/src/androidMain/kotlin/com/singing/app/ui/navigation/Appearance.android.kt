package com.singing.app.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import com.singing.app.navigation.base.extensions.NoNavigationScreen
import com.singing.app.navigation.base.extensions.TopBarScreen


@Composable
actual fun NavigationAppearance(
    navigator: Navigator,
    content: @Composable BoxScope.() -> Unit
) {
    val currentScreen = navigator.lastItemOrNull

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            if (currentScreen is TopBarScreen) {
                currentScreen.TopAppBar()
            }
        },
        bottomBar = {
            if (currentScreen !is NoNavigationScreen) {
                NavigationBar {
                    NavigationItems().forEach {
                        NavigationBarItem(
                            selected = if (currentScreen == null) {
                                false
                            } else {
                                areScreensSame(currentScreen, it.reference())
                            },
                            label = { it.title() },
                            icon = {
                                it.icon()
                            },
                            onClick = {
                                val screen = it.reference().screen

                                navigator.popAll()
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
