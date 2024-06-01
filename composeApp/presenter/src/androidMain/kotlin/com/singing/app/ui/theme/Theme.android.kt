package com.singing.app.ui.theme

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat

@Composable
internal actual fun SystemAppearance(scheme: ExtendedMaterialTheme, isDark: Boolean) {
    val view = LocalView.current
    val window = (view.context as Activity).window

    LaunchedEffect(isDark) {
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightStatusBars = !isDark
            isAppearanceLightNavigationBars = !isDark
        }
    }
}

@Composable
internal actual fun getAppTheme(isDark: Boolean): ExtendedMaterialTheme {
    return when {
        isDark -> DarkColorScheme
        else -> LightColorScheme
    }
}

@Composable
internal actual fun getScreenSize(): Size = Size(
    width = LocalConfiguration.current.screenWidthDp.toFloat(),
    height = LocalConfiguration.current.screenHeightDp.toFloat(),
)
