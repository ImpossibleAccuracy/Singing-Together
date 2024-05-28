package com.singing.app.ui.screen

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


val LocalWindowSize = compositionLocalOf { WindowSize.COMPACT }
val LocalSmallestWindowSize = compositionLocalOf { WindowSize.COMPACT }

enum class WindowSize(val dimens: AppDimens) {
    COMPACT(compactDimens),
    MEDIUM(mediumDimens),
    EXPANDED(expandedDimens)
}

fun computeWindowSize(windowWidth: Dp) = when {
    windowWidth < 600.dp -> WindowSize.COMPACT
    windowWidth < 840.dp -> WindowSize.MEDIUM
    else -> WindowSize.EXPANDED
}
