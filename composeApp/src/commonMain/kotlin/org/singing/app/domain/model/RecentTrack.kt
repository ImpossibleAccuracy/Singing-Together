package org.singing.app.domain.model

import com.singing.audio.player.model.AudioFile

data class RecentTrack(
    val file: AudioFile,
    val isFavourite: Boolean,
)
