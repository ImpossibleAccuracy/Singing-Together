package com.singing.feature.recording.setup.viewmodel

import com.singing.app.domain.model.RecentTrack
import com.singing.app.domain.model.TrackParseResult
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

data class SelectAudioUiState(
    val trackData: TrackParseResult? = null,
    val isParsing: Boolean = false,
    val tracks: PersistentList<RecentTrack> = persistentListOf(),
)