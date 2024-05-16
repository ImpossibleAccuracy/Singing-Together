package org.singing.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.app_name
import org.jetbrains.compose.resources.stringResource
import org.singing.app.ui.base.AppScreen
import org.singing.app.ui.navigation.getStartDestination
import org.singing.app.ui.theme.AppTheme


@Composable
internal expect fun getScreenTitle(screen: Screen): String

@Composable
internal expect fun isRootNavigationItem(screen: Screen): Boolean

@Composable
internal expect fun getTopAppBarColors(screen: Screen): Pair<Color, Color>?


@OptIn(ExperimentalMaterial3Api::class)
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
                    prevItem.onLeave()
                }
            }

            Scaffold(
                modifier = Modifier.systemBarsPadding(),
                containerColor = MaterialTheme.colorScheme.background,
                topBar = {
                    val colors = navigator.lastItemOrNull?.let { getTopAppBarColors(it) }

                    val containerColor = colors?.first ?: MaterialTheme.colorScheme.background
                    val contentColor = colors?.second ?: MaterialTheme.colorScheme.onBackground

                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = containerColor,
                            navigationIconContentColor = contentColor,
                            titleContentColor = contentColor,
                        ),
                        navigationIcon = {
                            val isRoot = navigator.lastItemOrNull?.let { isRootNavigationItem(it) } ?: false

                            IconButton(
                                onClick = {
                                    if (navigator.canPop && !isRoot) {
                                        onNavigateUp(navigator.lastItem)

                                        navigator.pop()
                                    }
                                }
                            ) {
                                val icon =
                                    if (isRoot) Icons.Filled.Menu
                                    else Icons.AutoMirrored.Filled.ArrowBack

                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                )
                            }
                        },
                        title = {
                            Text(
                                text = navigator.lastItemOrNull?.let { getScreenTitle(it) }
                                    ?: stringResource(Res.string.app_name),
                                style = MaterialTheme.typography.titleLarge,
                            )
                        }
                    )
                }
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    CurrentScreen()
                }
            }
        }
    }
}

private fun onNavigateUp(screen: Screen) {
    if (screen is AppScreen) {
        screen.onClose()
    }
}
