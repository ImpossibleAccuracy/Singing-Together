package org.singing.app.ui.screens.main.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.collections.immutable.ImmutableList
import org.singing.app.domain.model.RecentTrack
import org.singing.app.ui.base.Space
import org.singing.app.ui.views.base.track.TrackCard


private val itemShape
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.shapes.small

@Composable
fun RecentTracks(
    listModifier: Modifier = Modifier,
    tracks: ImmutableList<RecentTrack>,
    onFavouriteChange: (RecentTrack, Boolean) -> Unit,
) {
    Column {
        Text(
            text = "Recently used tracks",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
        )

        Space(8.dp)

        LazyColumn(
            modifier = listModifier,
        ) {
            itemsIndexed(tracks) { index, item ->
                TrackCard(
                    track = item,
                    onFavouriteChange = {
                        onFavouriteChange(item, it)
                    }
                )

                if (index != tracks.lastIndex) {
                    Space(8.dp)
                }
            }
        }
    }
}
