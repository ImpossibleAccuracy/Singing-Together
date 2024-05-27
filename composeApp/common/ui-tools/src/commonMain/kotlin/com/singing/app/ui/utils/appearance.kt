package com.singing.app.ui.utils

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.singing.app.ui.plus

fun Modifier.cardAppearance(
    shape: Shape,
    background: Color,
    padding: PaddingValues = PaddingValues(),
    border: BorderStroke? = null,
    onClick: (() -> Unit)? = null,
) = this then Modifier
    .plus(border?.let { Modifier.border(it, shape) })
    .clip(shape)
    .background(background)
    .clickable(enabled = onClick != null) { onClick?.invoke() }
    .padding(padding)
