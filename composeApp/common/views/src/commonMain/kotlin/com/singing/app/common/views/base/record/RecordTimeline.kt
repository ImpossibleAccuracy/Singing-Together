package com.singing.app.common.views.base.record


/*@Composable
fun RecordTimeline(
    modifier: Modifier = Modifier,
    isLazyColumn: Boolean = false,
    isTwoLineRecord: Boolean,
    points: ImmutableList<RecordPoint>,
    note: (Double) -> String,
) {
    Timeline(
        modifier = modifier,
        isLazyColumn = isLazyColumn,
        nodes = points,
        startNode = {
            Text(
                text = stringResource(Res.string.label_start),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
            )
        },
        finishNode = {
            Text(
                text = stringResource(Res.string.label_finish),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
            )
        },
        indicator = { indicatorModifier, color, position ->
            DefaultTimelineIndicator(
                indicatorModifier.offset(y = 2.dp),
                color,
                position,
            )
        },
        indicatorColor = { position ->
            if (position == 0 || position == points.size + 1) {
                MaterialTheme.colorScheme.secondary
            } else if (isTwoLineRecord) {
                val item = points[position - 1]

                when (item.accuracy) {
                    PointAccuracy.Best -> MaterialTheme.extended.timeline.best
                    PointAccuracy.Normal -> MaterialTheme.extended.timeline.normal
                    PointAccuracy.Bad -> MaterialTheme.extended.timeline.bad
                    PointAccuracy.Worst -> MaterialTheme.extended.timeline.worst
                    PointAccuracy.Undefined -> MaterialTheme.extended.timeline.undefined
                }
            } else {
                MaterialTheme.colorScheme.tertiary
            }
        },
        nodeLabel = { item, _ ->
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
    ) { item, _ ->
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = note(item.first),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium,
                )

                if (isTwoLineRecord && item.second != null) {
                    Text(
                        text = stringResource(
                            Res.string.label_expected_note,
                            note(item.first),
                            note(item.second!!),
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
            }

            Space(12.dp)

            Text(
                text = formatFrequency(item.first),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}*/
