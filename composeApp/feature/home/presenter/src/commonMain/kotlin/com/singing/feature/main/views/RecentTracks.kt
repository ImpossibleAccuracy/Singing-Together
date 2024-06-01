package com.singing.feature.main.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.singing.app.common.views.base.list.EmptyView
import com.singing.app.common.views.base.track.TrackCard
import com.singing.app.common.views.toTrackCardData
import com.singing.app.domain.model.RecentTrack
import com.singing.app.ui.screen.dimens
import com.singing.app.ui.screen.smallListSpacing
import com.singing.app.ui.utils.cardAppearance
import com.singing.feature.main.presenter.generated.resources.*
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


@Composable
fun RecentTracks(
    modifier: Modifier = Modifier,
    listModifier: Modifier = Modifier,
    tracks: ImmutableList<RecentTrack>,
    onFavouriteChange: (RecentTrack, Boolean) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1),
    ) {
        if (tracks.isEmpty()) {
            EmptyView(
                modifier = Modifier
                    .fillMaxWidth()
                    .cardAppearance(
                        shape = MaterialTheme.shapes.small,
                        background = MaterialTheme.colorScheme.tertiaryContainer,
                        padding = PaddingValues(
                            horizontal = MaterialTheme.dimens.dimen2,
                            vertical = MaterialTheme.dimens.dimen4,
                        )
                    ),
                icon = {
                    Icon(
                        modifier = Modifier.size(MaterialTheme.dimens.dimen4 * 2),
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

            LazyVerticalGrid(
                modifier = listModifier,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.smallListSpacing),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.smallListSpacing),
                columns = GridCells.Adaptive(minSize = 300.dp),
            ) {
                items(tracks) { item ->
                    TrackCard(
                        data = item.toTrackCardData(),
                        onFavouriteChange = {
                            onFavouriteChange(item, it)
                        }
                    )
                }
            }
        }
    }
}
