package org.singing.app.ui.theme

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
