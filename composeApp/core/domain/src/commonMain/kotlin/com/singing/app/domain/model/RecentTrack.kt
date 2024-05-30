package com.singing.app.domain.model

import com.singing.app.domain.model.stable.StableInstant
import com.singing.domain.model.AudioFile

data class RecentTrack(
    val id: Int,
    val createdAt: StableInstant,
    val audioFile: AudioFile,
    val isFavourite: Boolean,
)
