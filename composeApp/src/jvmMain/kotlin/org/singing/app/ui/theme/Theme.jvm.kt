package org.singing.app.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
internal actual fun SystemAppearance(scheme: ColorScheme, isDark: Boolean) {
    // No appearance on desktop
}

@Composable
internal actual fun getAppTheme(isDark: Boolean): ColorScheme =
    LightColorScheme // TODO: fix on production
//    if (isDark) DarkColorScheme
//    else LightColorScheme
