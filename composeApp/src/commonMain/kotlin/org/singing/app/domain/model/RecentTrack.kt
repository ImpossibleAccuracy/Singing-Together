package org.singing.app.domain.model

import com.singing.app.domain.model.AudioFile

data class RecentTrack(
    val audioFile: AudioFile,
    val isFavourite: Boolean,
)
