package org.singing.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*


internal val ExtendedColors = staticCompositionLocalOf { LightColorScheme }

internal val LocalThemeIsDark = compositionLocalOf { mutableStateOf(true) }


@Composable
internal fun AppTheme(
    content: @Composable () -> Unit
) {
    val systemIsDark = isSystemInDarkTheme()
    val isDarkState = remember { mutableStateOf(systemIsDark) }

    CompositionLocalProvider(
        LocalThemeIsDark provides isDarkState
    ) {
        val isDark by isDarkState

        val theme = getAppTheme(isDark)

        SystemAppearance(theme, isDark)

        CompositionLocalProvider(ExtendedColors provides theme) {
            MaterialTheme(
                colorScheme = theme.material,
                content = { Surface(content = content) }
            )
        }
    }
}

@Composable
internal expect fun SystemAppearance(scheme: ExtendedMaterialTheme, isDark: Boolean)

@Composable
internal expect fun getAppTheme(isDark: Boolean): ExtendedMaterialTheme
