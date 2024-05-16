package org.singing.app.ui.theme

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current

    return when {
        // TODO: add dynamic theme for android
//        (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) -> {
//            if (isDark) dynamicDarkColorScheme(context)
//            else dynamicLightColorScheme(context)
//        }

        isDark -> DarkColorScheme
        else -> LightColorScheme
    }
}
