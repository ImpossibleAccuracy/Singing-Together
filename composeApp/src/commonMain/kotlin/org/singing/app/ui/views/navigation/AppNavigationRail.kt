package org.singing.app.ui.views.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.ui.base.Space
import org.singing.app.ui.common.navigation.FabScreen
import org.singing.app.ui.navigation.NavigationItems
import org.singing.app.ui.navigation.isRootScreen


@Composable
fun AppNavigationRail(
    currentScreen: Screen?,
    navigate: (Screen?, Screen) -> Unit,
    onNavigationClick: (Screen) -> Unit,
) {
    NavigationRail(
        modifier = Modifier
            .requiredWidth(80.dp)
            .padding(
                top = 44.dp,
                bottom = 56.dp,
            )
    ) {
        IconButton(
            onClick = {
                if (currentScreen != null) {
                    onNavigationClick(currentScreen)
                }
            },
        ) {
            if (currentScreen != null && isRootScreen(currentScreen)) {
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

                    navigate(currentScreen, screen)
                }
            )
        }
    }
}
