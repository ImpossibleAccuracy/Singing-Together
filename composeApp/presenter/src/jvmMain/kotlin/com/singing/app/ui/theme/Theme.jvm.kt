package com.singing.app.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

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
internal actual fun getScreenSize(): DpSize =
    DpSize(
        width = LocalWindowInfo.current.containerSize.width.dp,
        height = LocalWindowInfo.current.containerSize.height.dp,
    )
