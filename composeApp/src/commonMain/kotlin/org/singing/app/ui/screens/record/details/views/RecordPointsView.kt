package org.singing.app.ui.screens.record.details.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.title_record_points
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.stringResource
import com.singing.app.domain.model.RecordPoint
import org.singing.app.ui.views.base.list.Loader
import org.singing.app.ui.views.base.record.RecordTimeline

@Composable
fun RecordPointsView(
    modifier: Modifier = Modifier,
    points: ImmutableList<RecordPoint>,
    isTwoLineRecord: Boolean,
    note: (Double) -> String,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.small)
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
            .padding(
                start = 24.dp,
                top = 16.dp,
                end = 24.dp,
                bottom = 16.dp,
            )
    ) {
        if (points.isEmpty()) {
            Loader(color = MaterialTheme.colorScheme.tertiary)
        } else {
            Text(
                text = stringResource(Res.string.title_record_points),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.titleMedium,
            )

            Spacer(Modifier.height(16.dp))

            RecordTimeline(
                isTwoLineRecord = isTwoLineRecord,
                points = points,
                note = note,
            )
        }
    }
}
