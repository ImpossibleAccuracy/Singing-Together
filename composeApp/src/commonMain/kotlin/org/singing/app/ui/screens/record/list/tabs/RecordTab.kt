package org.singing.app.ui.screens.record.list.tabs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.singing.app.domain.model.RecordData
import org.singing.app.domain.model.RecordPoint
import org.singing.app.ui.screens.record.list.RecordListViewModel
import org.singing.app.ui.screens.record.list.views.RecordTimeline

internal data class RecordTabData(
    val record: RecordData,
    val points: List<RecordPoint>? = null,
)

@Composable
internal fun RecordTab(
    viewModel: RecordListViewModel,
    data: RecordTabData,
    onDataUpdate: (RecordTabData) -> Unit,
) {
    LaunchedEffect(data.record) {
        val points = viewModel.getRecordPoints(data.record)

        onDataUpdate(
            data.copy(
                points = points
            )
        )
    }

    if (data.points == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.tertiary,
            )
        }
    } else {
        Text(
            text = "Record points",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleMedium,
        )

        Spacer(Modifier.height(16.dp))

        RecordTimeline(
            points = data.points,
            isTwoLineRecord = data.record is RecordData.Cover,
            note = {
                viewModel.getNote(it)
            }
        )
    }
}
