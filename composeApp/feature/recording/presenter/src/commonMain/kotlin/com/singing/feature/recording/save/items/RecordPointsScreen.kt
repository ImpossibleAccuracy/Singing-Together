package com.singing.feature.recording.save.items

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.koinScreenModel
import com.singing.app.common.views.base.timeline.DefaultTimelineIndicator
import com.singing.app.common.views.base.timeline.Timeline
import com.singing.app.common.views.shared.record.RecordTimelineItem
import com.singing.app.domain.model.RecordData
import com.singing.app.navigation.dialog.SkippableNavigationalDialogScreen
import com.singing.app.ui.formatTimeString
import com.singing.app.ui.screen.dimens
import com.singing.app.ui.theme.extended.extended
import com.singing.domain.model.PointAccuracy
import com.singing.domain.model.RecordPoint
import com.singing.feature.recording.save.RecordSaveAdditionalInfo
import com.singing.feature.recording.save.RecordSaveStrategy
import com.singing.feature.recording.save.RecordSaveViewModel


data class RecordPointsScreen(
    val data: RecordSaveAdditionalInfo,
) : SkippableNavigationalDialogScreen<RecordData>() {
    override fun buildNextPage() =
        when (data.user) {
            null -> ProcessRecordSaveScreen(
                data = data,
                strategy = RecordSaveStrategy.Locally
            )

            else -> SelectRecordStoreMethodScreen(data)
        }

    @Composable
    override fun Content() {
        val viewModel = koinScreenModel<RecordSaveViewModel>()

        RecordTimeline(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            points = data.history,
            note = viewModel::getNote,
        )
    }

    @Composable
    fun RecordTimeline(
        modifier: Modifier = Modifier,
        points: List<RecordPoint>,
        note: (Double) -> String,
    ) {
        Timeline(
            modifier = modifier,
            isLazyColumn = true,
            nodesCount = points.size,
            indicator = { indicatorModifier, color, position ->
                DefaultTimelineIndicator(
                    indicatorModifier.offset(y = MaterialTheme.dimens.dimen0_25),
                    color,
                    position,
                )
            },
            indicatorColor = { position ->
                val item = points[position]

                when (item.accuracy) {
                    PointAccuracy.Best -> MaterialTheme.extended.timeline.best
                    PointAccuracy.Normal -> MaterialTheme.extended.timeline.normal
                    PointAccuracy.Bad -> MaterialTheme.extended.timeline.bad
                    PointAccuracy.Worst -> MaterialTheme.extended.timeline.worst
                    PointAccuracy.Undefined -> MaterialTheme.extended.timeline.undefined
                }
            },
            nodeKey = { points[it].time },
            nodeLabel = { index ->
                val item = points[index]

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
            val item = points[index]

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1_5)
            ) {
                RecordTimelineItem(
                    first = item.first,
                    second = item.second,
                    note = note,
                )
            }
        }
    }
}
