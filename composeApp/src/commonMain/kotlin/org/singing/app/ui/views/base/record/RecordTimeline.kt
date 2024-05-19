package org.singing.app.ui.views.base.record

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.label_expected_note
import com.singing.app.composeapp.generated.resources.label_finish
import com.singing.app.composeapp.generated.resources.label_start
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.stringResource
import org.singing.app.domain.model.PointAccuracy
import org.singing.app.domain.model.RecordPoint
import org.singing.app.ui.base.Space
import org.singing.app.ui.base.formatFrequency
import org.singing.app.ui.base.formatTimeString
import org.singing.app.ui.theme.extended
import org.singing.app.ui.views.base.timeline.DefaultTimelineIndicator
import org.singing.app.ui.views.base.timeline.Timeline


@Composable
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
                            note(item.second),
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
}
