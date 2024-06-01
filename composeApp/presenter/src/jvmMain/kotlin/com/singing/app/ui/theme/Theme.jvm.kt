package com.singing.app.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalWindowInfo

@Composable
internal actual fun SystemAppearance(scheme: ExtendedMaterialTheme, isDark: Boolean) {
    // No appearance on desktop
}

@Composable
internal actual fun getAppTheme(isDark: Boolean): ExtendedMaterialTheme =
    if (isDark) DarkColorScheme
    else LightColorScheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal actual fun getScreenSize(): Size =
    Size(
        width = LocalWindowInfo.current.containerSize.width.toFloat(),
        height = LocalWindowInfo.current.containerSize.height.toFloat(),
    )
