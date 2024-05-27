package com.singing.app.domain.model

import com.singing.domain.model.AudioFile

data class RecentTrack(
    val audioFile: AudioFile,
    val isFavourite: Boolean,
)
