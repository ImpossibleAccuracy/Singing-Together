package com.singing.app.domain.model

import com.singing.domain.model.AudioFile
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.persistentMapOf

data class TrackParseResult(
    val selectedAudio: AudioFile,
    val data: PersistentMap<Long, Double> = persistentMapOf()
)
