package org.singing.app.ui.views.base.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
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
    nodes: ImmutableList<T>,
    startNode: (@Composable () -> Unit)? = null,
    finishNode: (@Composable () -> Unit)? = null,
    indicator: IndicatorView = DefaultTimelineIndicator,
    nodeSpacing: Dp = 6.dp,
    indicatorSize: Dp = 24.dp,
    indicatorColor: (@Composable (position: Int) -> Color)? = null,
    nodeLabel: (@Composable (item: T, position: Int) -> Unit)? = null,
    nodeContent: @Composable (item: T, position: Int) -> Unit,
) {
    val density = LocalDensity.current.density

    var labelSize by remember { mutableStateOf(0.dp) }

    val nodesOffset = if (startNode == null) 0 else 1

    Column(
        modifier = modifier,
    ) {
        if (startNode != null) {
            val nodeIndicatorColor = (indicatorColor?.invoke(0) ?: MaterialTheme.colorScheme.secondary)

            TimelineNode(
                density = density,
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
                density = density,
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
                    val actualSize = (it.width / density).dp

                    if (actualSize > labelSize) {
                        labelSize = actualSize
                    }
                },
                label = nodeLabel?.let { label ->
                    {
                        label(item, index)
                    }
                },
                content = {
                    nodeContent(item, index)
                },
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
                density = density,
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
    density: Float,
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
    val circleRadius = indicatorSize.value / 2 * density

    var lineOffsetX by remember { mutableStateOf(0f) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .drawWithCache {
                val start = Offset(
                    x = lineOffsetX + circleRadius,
                    y = circleRadius * 2 + nodeSpacing.value * density,
                )

                val end = Offset(
                    x = lineOffsetX + circleRadius,
                    y = size.height,
                )

                onDrawBehind {
                    if (position != TimelineNodePosition.LAST) {
                        drawLine(
                            color = indicatorColor,
                            strokeWidth = density,
                            start = start,
                            end = end,
                        )
                    }
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
            },
    ) {
        if (isLabelsEnabled) {
            Box(
                modifier = Modifier
                    .widthIn(min = labelSize)
                    .onSizeChanged(labelSizeReady)
            ) {
                label?.let {
                    it()
                }
            }

            Space(16.dp)
        }

        Box(
            modifier = Modifier
                .size(indicatorSize)
                .onGloballyPositioned {
                    lineOffsetX = it.positionInParent().x
                }
        ) {
            indicator(Modifier.fillMaxSize())
        }

        Space(16.dp)

        Column(
            modifier = Modifier.weight(1f)
        ) {
            content()
        }
    }
}
