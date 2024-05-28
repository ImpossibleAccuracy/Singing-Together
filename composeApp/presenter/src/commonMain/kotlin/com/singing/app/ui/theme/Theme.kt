package com.singing.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.min
import com.singing.app.ui.screen.LocalDimensions
import com.singing.app.ui.screen.LocalSmallestWindowSize
import com.singing.app.ui.screen.LocalWindowSize
import com.singing.app.ui.screen.computeWindowSize
import com.singing.app.ui.theme.extended.ExtendedColors


internal val LocalThemeIsDark = compositionLocalOf { mutableStateOf(true) }


@Composable
internal fun AppTheme(
    content: @Composable () -> Unit
) {
    val systemIsDark = isSystemInDarkTheme()
    val isDarkTheme = remember { mutableStateOf(systemIsDark) }

    CompositionLocalProvider(LocalThemeIsDark provides isDarkTheme) {
        val theme = getAppTheme(isDarkTheme.value)

        val screenSize = getScreenSize()
        val windowSize = computeWindowSize(screenSize.width)
        val smallestWindowSize = computeWindowSize(min(screenSize.width, screenSize.height))

        CompositionLocalProvider(
            ExtendedColors provides theme.extended,
            LocalWindowSize provides windowSize,
            LocalSmallestWindowSize provides smallestWindowSize,
            LocalDimensions provides windowSize.dimens,
        ) {
            SystemAppearance(theme, isDarkTheme.value)

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

@Composable
internal expect fun getScreenSize(): DpSize
