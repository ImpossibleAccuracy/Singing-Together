package org.singing.app.ui.theme

import androidx.compose.runtime.Composable

@Composable
internal actual fun SystemAppearance(scheme: ExtendedMaterialTheme, isDark: Boolean) {
    // No appearance on desktop
}

@Composable
internal actual fun getAppTheme(isDark: Boolean): ExtendedMaterialTheme =
    LightColorScheme // TODO: fix on production
//    if (isDark) DarkColorScheme
//    else LightColorScheme
