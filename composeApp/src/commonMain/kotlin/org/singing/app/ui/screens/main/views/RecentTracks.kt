package org.singing.app.ui.screens.main.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.singing.app.composeapp.generated.resources.*
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.domain.model.RecentTrack
import org.singing.app.ui.base.Space
import org.singing.app.ui.base.cardAppearance
import org.singing.app.ui.views.base.list.EmptyView
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
        if (tracks.isEmpty()) {
            EmptyView(
                modifier = Modifier
                    .fillMaxWidth()
                    .cardAppearance(
                        shape = MaterialTheme.shapes.small,
                        background = MaterialTheme.colorScheme.tertiaryContainer,
                        padding = PaddingValues(
                            horizontal = 16.dp,
                            vertical = 36.dp,
                        )
                    ),
                icon = {
                    Icon(
                        modifier = Modifier.size(64.dp),
                        imageVector = vectorResource(Res.drawable.baseline_folder_open_black_24dp),
                        tint = MaterialTheme.colorScheme.tertiary,
                        contentDescription = "",
                    )
                },
                title = stringResource(Res.string.title_empty_tracks),
                subtitle = stringResource(Res.string.subtitle_empty_tracks),
            )
        } else {
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
}
