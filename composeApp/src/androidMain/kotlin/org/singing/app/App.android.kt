package org.singing.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen

@Composable
internal actual fun getScreenTitle(screen: Screen): String {
    return screen.key
}

@Composable
@ReadOnlyComposable
internal actual fun isRootNavigationItem(screen: Screen): Boolean {
    return true
}

@Composable
@ReadOnlyComposable
internal actual fun getTopAppBarColors(screen: Screen): Pair<Color, Color>? {
    return null
}
