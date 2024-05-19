package org.singing.app.ui.views.base.track

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import org.singing.app.domain.model.RecentTrack
import org.singing.app.ui.base.cardAppearance
import org.singing.app.ui.base.formatTimeString

@Composable
fun TrackCard(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    track: RecentTrack,
    onFavouriteChange: (Boolean) -> Unit,
) {
    Box(
        modifier = modifier
            .cardAppearance(
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                shape = shape,
                background = MaterialTheme.colorScheme.surfaceContainerLow,
                padding = PaddingValues(
                    horizontal = 12.dp,
                    vertical = 8.dp,
                )
            )
    ) {
        TrackListItem(
            filename = track.audioFile.name,
            duration = formatTimeString(track.audioFile.duration),
            isFavourite = track.isFavourite,
            onFavouriteChange = onFavouriteChange,
        )
    }
}
