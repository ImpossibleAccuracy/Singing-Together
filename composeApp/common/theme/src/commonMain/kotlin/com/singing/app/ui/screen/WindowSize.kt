package com.singing.app.ui.screen

import androidx.compose.runtime.compositionLocalOf


val LocalWindowSize = compositionLocalOf { WindowSize.COMPACT }

enum class WindowSize(
    val dimens: AppDimens,
    val width: Int,
) {
    COMPACT(compactDimens, 600),
    MEDIUM(mediumDimens, 840),
    EXPANDED(expandedDimens, 0)
}

fun computeWindowSize(windowWidth: Float): WindowSize =
    WindowSize.entries
        .firstOrNull { it.width >= windowWidth }
        ?: WindowSize.EXPANDED
