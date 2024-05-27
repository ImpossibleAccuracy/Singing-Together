package com.singing.app.ui.utils

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Suppress("UnusedReceiverParameter")
@Composable
fun ColumnScope.Space(value: Dp) =
    Spacer(Modifier.height(value))

@Suppress("UnusedReceiverParameter")
@Composable
fun RowScope.Space(value: Dp) =
    Spacer(Modifier.width(value))

@Suppress("UnusedReceiverParameter")
@Composable
fun ColumnScope.Divider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.outlineVariant,
) = HorizontalDivider(
    modifier = modifier,
    color = color,
)

@Suppress("UnusedReceiverParameter")
@Composable
fun RowScope.Divider(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.outlineVariant,
) = VerticalDivider(
    modifier = modifier,
    color = color,
)

