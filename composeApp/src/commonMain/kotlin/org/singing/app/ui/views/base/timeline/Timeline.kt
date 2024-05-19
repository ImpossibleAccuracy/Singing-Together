package org.singing.app.ui.views.base.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
    isLazyColumn: Boolean = false,
    startNode: (@Composable () -> Unit)? = null,
    finishNode: (@Composable () -> Unit)? = null,
    indicator: IndicatorView = DefaultTimelineIndicator,
    nodeSpacing: Dp = 6.dp,
    indicatorSize: Dp = 24.dp,
    indicatorColor: (@Composable (position: Int) -> Color)? = null,
    nodeLabel: (@Composable (item: T, position: Int) -> Unit)? = null,
    nodeContent: @Composable (item: T, position: Int) -> Unit,
) {
    val density = LocalDensity.current
    val labelSize = remember { mutableStateOf(0.dp) }

    val defaultColor = MaterialTheme.colorScheme.secondary

    if (isLazyColumn) {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(nodeSpacing),
        ) {
            timelineContent(
                itemWrapper = { item { it() } },
                density = density.density,
                labelSize = labelSize,
                nodes = nodes,
                startNode = startNode,
                finishNode = finishNode,
                indicator = indicator,
                nodeSpacing = nodeSpacing,
                indicatorSize = indicatorSize,
                indicatorColor = { indicatorColor?.invoke(it) ?: defaultColor },
                nodeLabel = nodeLabel,
                nodeContent = nodeContent
            )
        }
    } else {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(nodeSpacing),
        ) {
            timelineContent(
                itemWrapper = { it() },
                density = density.density,
                labelSize = labelSize,
                nodes = nodes,
                startNode = startNode,
                finishNode = finishNode,
                indicator = indicator,
                nodeSpacing = nodeSpacing,
                indicatorSize = indicatorSize,
                indicatorColor = { indicatorColor?.invoke(it) ?: defaultColor },
                nodeLabel = nodeLabel,
                nodeContent = nodeContent
            )
        }
    }
}


inline fun <T> timelineContent(
    itemWrapper: (@Composable () -> Unit) -> Unit,
    density: Float,
    labelSize: MutableState<Dp>,
    nodes: ImmutableList<T>,
    noinline startNode: (@Composable () -> Unit)? = null,
    noinline finishNode: (@Composable () -> Unit)? = null,
    crossinline indicator: IndicatorView,
    nodeSpacing: Dp = 6.dp,
    indicatorSize: Dp = 24.dp,
    crossinline indicatorColor: @Composable (position: Int) -> Color,
    noinline nodeLabel: (@Composable (item: T, position: Int) -> Unit)? = null,
    noinline nodeContent: @Composable (item: T, position: Int) -> Unit,
) {
    val nodesOffset = if (startNode == null) 0 else 1

    if (startNode != null) {
        itemWrapper {
            val nodeIndicatorColor = indicatorColor.invoke(0)

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
                labelSize = labelSize.value,
                labelSizeReady = {},
                content = startNode,
            )
        }
    }

    nodes.forEachIndexed { index, item ->
        val nodePosition = when {
            (index == 0 && startNode == null) -> TimelineNodePosition.FIRST
            (index == nodes.lastIndex && finishNode == null) -> TimelineNodePosition.LAST
            else -> TimelineNodePosition.MIDDLE
        }

        itemWrapper {
            val nodeIndicatorColor = indicatorColor.invoke(index + nodesOffset)

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
                labelSize = labelSize.value,
                labelSizeReady = {
                    val actualSize = (it.width / density).dp

                    if (actualSize > labelSize.value) {
                        labelSize.value = actualSize
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
        }
    }

    if (finishNode != null) {
        itemWrapper {
            val nodeIndicatorColor = indicatorColor(nodes.lastIndex + nodesOffset + 1)

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
                labelSize = labelSize.value,
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
