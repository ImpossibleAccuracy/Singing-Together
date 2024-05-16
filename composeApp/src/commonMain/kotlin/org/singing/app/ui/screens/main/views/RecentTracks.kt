package org.singing.app.ui.screens.main.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.singing.app.domain.model.RecentTrack
import org.singing.app.ui.base.Space
import org.singing.app.ui.base.formatTimeString
import org.singing.app.ui.views.base.track.TrackItem


@Composable
fun RecentTracks(
    listModifier: Modifier = Modifier,
    tracks: List<RecentTrack>,
    onFavouriteChange: (RecentTrack, Boolean) -> Unit,
) {
    Column {
        Text(
            text = "Recently used tracks",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
        )

        Space(8.dp)

        LazyColumn(
            modifier = listModifier,
        ) {
            itemsIndexed(tracks) { index, item ->
                Box(
                    modifier = Modifier
                        .clip(shape = MaterialTheme.shapes.medium)
                        .background(color = MaterialTheme.colorScheme.surfaceContainer)
                        .padding(
                            horizontal = 12.dp,
                            vertical = 8.dp,
                        )
                ) {
                    TrackItem(
                        filename = item.audioFile.name,
                        duration = formatTimeString(item.audioFile.duration),
                        isFavourite = item.isFavourite,
                        onFavouriteChange = {
                            onFavouriteChange(item, it)
                        }
                    )
                }

                if (index != tracks.lastIndex) {
                    Space(8.dp)
                }
            }
        }
    }
}
