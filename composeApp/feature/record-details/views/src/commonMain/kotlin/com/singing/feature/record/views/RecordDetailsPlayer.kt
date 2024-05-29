package com.singing.feature.record.views

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.singing.app.common.views.shared.player.PlayerView
import com.singing.app.common.views.toPlayerController
import com.singing.app.domain.features.RecordPlayer
import com.singing.app.domain.model.RecordData

@Composable
internal fun RecordDetailsPlayer(
    modifier: Modifier = Modifier,
    player: RecordPlayer,
    record: RecordData,
) {
    PlayerView(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.secondary,
        inactiveTrackColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        playerController = player.toPlayerController(record)
    )
}