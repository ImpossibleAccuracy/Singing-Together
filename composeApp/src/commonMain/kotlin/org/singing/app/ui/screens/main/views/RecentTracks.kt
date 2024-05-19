package org.singing.app.ui.screens.main.views

import androidx.compose.foundation.layout.Arrangement
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
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.title_recently_used_tracks
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.stringResource
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
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(Res.string.title_recently_used_tracks),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
        )

        LazyColumn(modifier = listModifier) {
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
