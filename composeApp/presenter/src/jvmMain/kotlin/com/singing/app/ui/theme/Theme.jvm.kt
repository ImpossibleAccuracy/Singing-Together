package com.singing.app.ui.theme

import androidx.compose.runtime.Composable

@Composable
internal actual fun SystemAppearance(scheme: ExtendedMaterialTheme, isDark: Boolean) {
    // No appearance on desktop
}

@Composable
internal actual fun getAppTheme(isDark: Boolean): ExtendedMaterialTheme =
    if (isDark) DarkColorScheme
    else LightColorScheme
