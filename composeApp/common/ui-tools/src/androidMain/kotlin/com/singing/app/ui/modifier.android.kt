package com.singing.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize

@Composable
actual fun getScreenSize(): IntSize {
    val density = LocalDensity.current.density

    return IntSize(
        width = (LocalConfiguration.current.screenWidthDp * density).toInt(),
        height = (LocalConfiguration.current.screenHeightDp * density).toInt(),
    )
}
