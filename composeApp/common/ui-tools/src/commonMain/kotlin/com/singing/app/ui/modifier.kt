package com.singing.app.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Dp


fun Modifier.plus(
    other: Modifier?
) = when (other) {
    null -> this
    else -> this.then(other)
}

@Composable
fun Modifier.plus(expression: Boolean, func: @Composable Modifier.() -> Unit): Modifier = apply {
    if (expression) func(this)
}

fun Modifier.connectVerticalNestedScroll(
    maxHeight: Dp,
    scrollState: ScrollState,
) = this then Modifier
    .heightIn(max = maxHeight)
    .nestedScroll(
        connection = object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (scrollState.canScrollForward && available.y < 0) {
                    val consumed = scrollState.dispatchRawDelta(-available.y)

                    return Offset(x = 0f, y = -consumed)
                }

                return Offset.Zero
            }
        }
    )
