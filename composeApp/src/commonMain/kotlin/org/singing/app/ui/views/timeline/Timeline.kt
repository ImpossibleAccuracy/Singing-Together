package org.singing.app.ui.views.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import org.singing.app.ui.base.Space


enum class TimelineNodePosition {
    FIRST,
    MIDDLE,
    LAST
}

private typealias IndicatorView = @Composable (modifier: Modifier, color: Color, position: Int) -> Unit


val DefaultTimelineIndicator: IndicatorView = @Composable { modifier, color, _ ->
    Box(
        modifier = modifier
            .clip(shape = RoundedCornerShape(50))
            .background(color)
            .padding(5.dp)
            .clip(shape = RoundedCornerShape(50))
            .background(MaterialTheme.colorScheme.background)
    )
}


@Composable
fun <T> Timeline(
    modifier: Modifier = Modifier,
    nodes: List<T>,
    startNode: (@Composable () -> Unit)? = null,
    finishNode: (@Composable () -> Unit)? = null,
    indicator: IndicatorView = DefaultTimelineIndicator,
    nodeSpacing: Dp = 6.dp,
    indicatorSize: Dp = 24.dp,
    indicatorColor: (@Composable (position: Int) -> Color)? = null,
    nodeLabel: (@Composable (item: T, position: Int) -> Unit)? = null,
    nodeContent: @Composable (item: T, position: Int) -> Unit,
) {
    var labelSize by remember { mutableStateOf(0.dp) }

    val nodesOffset = if (startNode == null) 0 else 1

    Column(
        modifier = modifier,
    ) {
        if (startNode != null) {
            val nodeIndicatorColor = (indicatorColor?.invoke(0) ?: MaterialTheme.colorScheme.secondary)

            TimelineNode(
                position = TimelineNodePosition.FIRST,
                nodeSpacing = nodeSpacing,
                indicatorSize = indicatorSize,
                indicatorColor = nodeIndicatorColor,
                indicator = {
                    indicator(it, nodeIndicatorColor, 0)
                },
                isLabelsEnabled = nodeLabel != null,
                labelSize = labelSize,
                labelSizeReady = {},
                content = startNode,
            )

            Space(nodeSpacing)
        }

        nodes.forEachIndexed { index, item ->
            val nodePosition = when {
                (index == 0 && startNode == null) -> TimelineNodePosition.FIRST
                (index == nodes.lastIndex && finishNode == null) -> TimelineNodePosition.LAST
                else -> TimelineNodePosition.MIDDLE
            }

            val nodeIndicatorColor = indicatorColor?.invoke(index + nodesOffset)
                ?: MaterialTheme.colorScheme.secondary

            TimelineNode(
                position = nodePosition,
                nodeSpacing = nodeSpacing,
                indicatorSize = indicatorSize,
                indicatorColor = nodeIndicatorColor,
                indicator = {
                    indicator(it, nodeIndicatorColor, index + nodesOffset)
                },
                isLabelsEnabled = nodeLabel != null,
                labelSize = labelSize,
                labelSizeReady = {
                    if (it.width > labelSize.value) {
                        labelSize = it.width.dp
                    }
                },
                label = nodeLabel?.let { label ->
                    {
                        label(item, index)
                    }
                },
                content = {
                    nodeContent(item, index)
                }
            )

            if (index != nodes.lastIndex || finishNode != null) {
                Space(nodeSpacing)
            }
        }

        if (finishNode != null) {
            val nodeIndicatorColor = (indicatorColor?.invoke(
                nodes.lastIndex + nodesOffset + 1
            ) ?: MaterialTheme.colorScheme.secondary)

            TimelineNode(
                position = TimelineNodePosition.LAST,
                nodeSpacing = nodeSpacing,
                indicatorSize = indicatorSize,
                indicatorColor = nodeIndicatorColor,
                indicator = {
                    indicator(it, nodeIndicatorColor, 0)
                },
                isLabelsEnabled = nodeLabel != null,
                labelSize = labelSize,
                labelSizeReady = {},
                content = finishNode,
            )
        }
    }
}


@Composable
fun TimelineNode(
    position: TimelineNodePosition,
    nodeSpacing: Dp,
    indicatorSize: Dp,
    indicatorColor: Color,
    indicator: @Composable (modifier: Modifier) -> Unit,
    isLabelsEnabled: Boolean,
    labelSize: Dp,
    labelSizeReady: (IntSize) -> Unit,
    label: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val circleRadius = indicatorSize.value / 2

    var lineOffsetX by remember { mutableStateOf(0f) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                if (position != TimelineNodePosition.LAST) {
                    drawLine(
                        color = indicatorColor,
                        start = Offset(
                            x = lineOffsetX + circleRadius,
                            y = circleRadius * 2 + nodeSpacing.value,
                        ),
                        end = Offset(
                            x = lineOffsetX + circleRadius,
                            y = size.height,
                        ),
                    )
                }
            }
            .let {
                if (position == TimelineNodePosition.LAST) {
                    it
                } else {
                    it.padding(
                        bottom = 32.dp,
                    )
                }
            }) {

        if (isLabelsEnabled) {
            Box(
                modifier = Modifier
                    .widthIn(min = labelSize)
                    .onSizeChanged { labelSizeReady(it) }
            ) {
                label?.let {
                    it()
                }
            }

            Space(16.dp)
        }

        indicator(
            Modifier
                .size(indicatorSize)
                .onGloballyPositioned {
                    lineOffsetX = it.positionInParent().x
                },
        )

        Space(16.dp)

        Column(
            modifier = Modifier.weight(1f)
        ) {
            content()
        }
    }
}
