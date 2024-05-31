package com.singing.feature.recording.setup.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.singing.app.base.ComposeFile
import com.singing.app.common.views.base.track.TrackListItem
import com.singing.app.domain.model.RecentTrack
import com.singing.app.feature.FilePicker
import com.singing.app.ui.formatTimeString
import com.singing.app.ui.screen.dimens
import com.singing.app.ui.screen.listSpacing
import com.singing.config.track.TrackProperties
import com.singing.feature.recording.setup.presenter.generated.resources.Res
import com.singing.feature.recording.setup.presenter.generated.resources.action_select_track
import com.singing.feature.recording.setup.presenter.generated.resources.baseline_folder_open_black_24dp
import com.singing.feature.recording.setup.presenter.generated.resources.label_no_saved_tracks
import com.singing.feature.recording.setup.presenter.generated.resources.label_or
import com.singing.feature.recording.setup.presenter.generated.resources.label_pick_from_recent
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


@Composable
fun AudioChooser(
    recentTracks: PersistentList<RecentTrack>,
    onFileSelected: (ComposeFile) -> Unit,
) {
    var showAudioTrackPicker by remember { mutableStateOf(false) }

    FilePicker(
        show = showAudioTrackPicker,
        fileExtensions = TrackProperties.allowedSoundFormats.toPersistentList(),
        onFileSelected = { inputFile ->
            if (inputFile != null) {
                onFileSelected(inputFile)
            }
        }
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen3),
    ) {
        FilledTonalButton(
            modifier = Modifier
                .widthIn(400.dp, 700.dp)
                .heightIn(min = 64.dp),
            shape = MaterialTheme.shapes.medium,
            contentPadding = PaddingValues(
                horizontal = MaterialTheme.dimens.dimen3,
                vertical = MaterialTheme.dimens.dimen1
            ),
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            onClick = {
                showAudioTrackPicker = true
            }
        ) {
            Text(
                text = stringResource(Res.string.action_select_track),
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.weight(1f))

            Icon(
                imageVector = vectorResource(Res.drawable.baseline_folder_open_black_24dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                contentDescription = "",
            )
        }

        if (recentTracks.isNotEmpty()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                HorizontalDivider(Modifier.weight(1f))

                Text(
                    modifier = Modifier.padding(horizontal = MaterialTheme.dimens.dimen1_5),
                    text = stringResource(Res.string.label_or),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.titleMedium,
                )

                HorizontalDivider(Modifier.weight(1f))
            }

            Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1)) {
                Text(
                    text = stringResource(Res.string.label_pick_from_recent),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelMedium,
                )

                if (recentTracks.isEmpty()) {
                    Text(
                        text = stringResource(Res.string.label_no_saved_tracks),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium,
                    )
                } else {
                    RecentTracks(recentTracks, onFileSelected)
                }
            }
        }
    }
}

@Composable
private fun RecentTracks(
    recentTracks: PersistentList<RecentTrack>,
    onFileSelected: (ComposeFile) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 250.dp),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen0_5),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.listSpacing),
    ) {
        items(recentTracks) { track ->
            TrackListItem(
                modifier = Modifier
                    .clip(shape = MaterialTheme.shapes.medium)
                    .clickable {
                        onFileSelected(track.audioFile.file)
                    }
                    .padding(horizontal = 12.dp),
                filename = track.audioFile.name,
                duration = formatTimeString(track.audioFile.duration),
            )
        }
    }
}

