package com.singing.feature.record.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.itemKey
import com.singing.app.common.views.base.list.EmptyView
import com.singing.app.common.views.base.list.Loader
import com.singing.app.common.views.base.timeline.DefaultTimelineIndicator
import com.singing.app.common.views.base.timeline.Timeline
import com.singing.app.common.views.shared.record.RecordTimelineItem
import com.singing.app.ui.formatTimeString
import com.singing.app.ui.screen.dimens
import com.singing.app.ui.theme.extended.extended
import com.singing.domain.model.PointAccuracy
import com.singing.domain.model.RecordPoint
import com.singing.feature.record.views.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun RecordPointsView(
    modifier: Modifier = Modifier,
    points: LazyPagingItems<RecordPoint>,
    isLazyColumn: Boolean,
    note: (Double) -> String,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.small)
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
            .padding(
                start = MaterialTheme.dimens.dimen3,
                top = MaterialTheme.dimens.dimen2,
                end = MaterialTheme.dimens.dimen3,
                bottom = MaterialTheme.dimens.dimen2,
            ),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen2),
    ) {
        Text(
            text = stringResource(Res.string.title_record_points),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleMedium,
        )

        RecordTimeline(
            modifier = Modifier.heightIn(max = 10000.dp),
            points = points,
            isLazyColumn = isLazyColumn,
            note = note,
        )
    }
}

@Composable
internal fun RecordTimeline(
    modifier: Modifier = Modifier,
    points: LazyPagingItems<RecordPoint>,
    isLazyColumn: Boolean,
    note: (Double) -> String,
) {
    with(points.loadState) {
        when {
            refresh is LoadState.Loading -> {
                Loader(Modifier.fillMaxSize())
            }

            refresh is LoadState.Error || append is LoadState.Error -> {
                EmptyView(
                    title = stringResource(Res.string.common_error_title),
                    subtitle = stringResource(Res.string.common_error_subtitle),
                )
            }

            refresh is LoadState.NotLoading && points.itemCount < 1 -> {
                EmptyView(
                    title = stringResource(Res.string.common_no_data_title),
                    subtitle = stringResource(Res.string.common_no_data_subtitle),
                )
            }

            refresh is LoadState.NotLoading -> {
                Timeline(
                    modifier = modifier,
                    isLazyColumn = isLazyColumn,
                    nodesCount = points.itemCount,
                    startNode = {
                        Text(
                            text = stringResource(Res.string.label_start),
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    },
                    finishNode = {
                        if (append is LoadState.Loading) {
                            Loader(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        MaterialTheme.dimens.dimen3,
                                        MaterialTheme.dimens.dimen2
                                    )
                            )
                        } else {
                            Text(
                                text = stringResource(Res.string.label_finish),
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.titleMedium,
                            )
                        }
                    },
                    indicator = { indicatorModifier, color, position ->
                        DefaultTimelineIndicator(
                            indicatorModifier.offset(y = MaterialTheme.dimens.dimen0_25),
                            color,
                            position,
                        )
                    },
                    indicatorColor = { position ->
                        if (position == 0 || position == points.itemCount + 1) {
                            MaterialTheme.colorScheme.secondary
                        } else {
                            val item = points[position - 1]

                            when (item?.accuracy) {
                                PointAccuracy.Best -> MaterialTheme.extended.timeline.best
                                PointAccuracy.Normal -> MaterialTheme.extended.timeline.normal
                                PointAccuracy.Bad -> MaterialTheme.extended.timeline.bad
                                PointAccuracy.Worst -> MaterialTheme.extended.timeline.worst
                                PointAccuracy.Undefined -> MaterialTheme.extended.timeline.undefined
                                null -> MaterialTheme.colorScheme.tertiary
                            }
                        }
                    },
                    nodeKey = points.itemKey { it.time },
                    nodeLabel = { index ->
                        val item = points[index]!!

                        Column(
                            modifier = Modifier
                                .padding(top = 3.dp)
                        ) {
                            Text(
                                text = formatTimeString(item.time),
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.labelMedium,
                            )
                        }
                    },
                ) { index ->
                    val item = points[index]!!

                    RecordTimelineItem(item.first, item.second, note)
                }
            }
        }
    }
}


