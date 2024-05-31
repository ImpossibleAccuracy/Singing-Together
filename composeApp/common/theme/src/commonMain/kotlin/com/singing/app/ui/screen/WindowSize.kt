package com.singing.app.ui.screen

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


val LocalWindowSize = compositionLocalOf { WindowSize.COMPACT }
val LocalSmallestWindowSize = compositionLocalOf { WindowSize.COMPACT }

enum class WindowSize(
    val dimens: AppDimens,
    val width: Dp,
) {
    COMPACT(compactDimens, 600.dp),
    MEDIUM(mediumDimens, 840.dp),
    EXPANDED(expandedDimens, 0.dp)
}

fun computeWindowSize(windowWidth: Dp) = WindowSize.entries
    .asReversed()
    .first {
        it.width <= windowWidth
    }
