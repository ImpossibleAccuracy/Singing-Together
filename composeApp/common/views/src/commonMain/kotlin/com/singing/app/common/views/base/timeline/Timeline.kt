package com.singing.app.common.views.base.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.singing.app.ui.screen.dimens
import com.singing.app.ui.screen.icon
import com.singing.app.ui.screen.listSpacing


enum class TimelineNodePosition {
    FIRST,
    MIDDLE,
    LAST
}

typealias IndicatorView = @Composable (modifier: Modifier, color: Color, position: Int) -> Unit


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
fun Timeline(
    modifier: Modifier = Modifier,
    nodesCount: Int,
    isLazyColumn: Boolean = false,
    startNode: (@Composable () -> Unit)? = null,
    finishNode: (@Composable () -> Unit)? = null,
    indicator: IndicatorView = DefaultTimelineIndicator,
    nodeSpacing: Dp = MaterialTheme.dimens.listSpacing / 2,
    indicatorSize: Dp = MaterialTheme.dimens.icon,
    indicatorColor: (@Composable (position: Int) -> Color)? = null,
    nodeLabel: (@Composable (position: Int) -> Unit)? = null,
    nodeKey: ((position: Int) -> Any?)? = null,
    nodeContent: @Composable (position: Int) -> Unit,
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
                itemWrapper = { index, item ->
                    item(key = {
                        when {
                            startNode != null && index == 0 -> "START_NODE"
                            finishNode != null && index == nodesCount + (if (startNode == null) 1 else 2) -> "FINISH_NODE"

                            startNode != null && index > 0 -> nodeKey?.invoke(index - 1)
                            else -> nodeKey?.invoke(index)
                        }
                    }) {
                        item()
                    }
                },
                density = density.density,
                labelSize = labelSize,
                nodesCount = nodesCount,
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
                itemWrapper = { _, item -> item() },
                density = density.density,
                labelSize = labelSize,
                nodesCount = nodesCount,
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


inline fun timelineContent(
    itemWrapper: (Int, @Composable () -> Unit) -> Unit,
    density: Float,
    labelSize: MutableState<Dp>,
    nodesCount: Int,
    noinline startNode: (@Composable () -> Unit)? = null,
    noinline finishNode: (@Composable () -> Unit)? = null,
    crossinline indicator: IndicatorView,
    nodeSpacing: Dp,
    indicatorSize: Dp,
    crossinline indicatorColor: @Composable (position: Int) -> Color,
    noinline nodeLabel: (@Composable (position: Int) -> Unit)? = null,
    noinline nodeContent: @Composable (position: Int) -> Unit,
) {
    val nodesOffset = if (startNode == null) 0 else 1

    if (startNode != null) {
        itemWrapper(0) {
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

    repeat(nodesCount) { index ->
        val nodePosition = when {
            (index == 0 && startNode == null) -> TimelineNodePosition.FIRST
            (index == nodesCount - 1 && finishNode == null) -> TimelineNodePosition.LAST
            else -> TimelineNodePosition.MIDDLE
        }

        itemWrapper(index + nodesOffset) {
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
                        label(index)
                    }
                },
                content = {
                    nodeContent(index)
                },
            )
        }
    }

    if (finishNode != null) {
        itemWrapper(nodesCount + nodesOffset) {
            val nodeIndicatorColor = indicatorColor(nodesCount + nodesOffset)

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
                        bottom = MaterialTheme.dimens.dimen4,
                    )
                }
            },
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen2),
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

        Column(
            modifier = Modifier.weight(1f)
        ) {
            content()
        }
    }
}
