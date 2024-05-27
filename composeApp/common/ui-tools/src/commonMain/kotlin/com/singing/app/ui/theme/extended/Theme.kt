package com.singing.app.ui.theme.extended

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf


val ExtendedColors = staticCompositionLocalOf<ExtendedMaterialColors> {
    throw NullPointerException()
}

val MaterialTheme.extended: ExtendedMaterialColors
    @Composable
    @ReadOnlyComposable
    get() = ExtendedColors.current
