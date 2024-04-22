package org.singing.app.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
internal actual fun SystemAppearance(scheme: ColorScheme, isDark: Boolean) {
}

@Composable
internal actual fun getAppTheme(isDark: Boolean): ColorScheme =
    if (isDark) DarkColorScheme
    else LightColorScheme
