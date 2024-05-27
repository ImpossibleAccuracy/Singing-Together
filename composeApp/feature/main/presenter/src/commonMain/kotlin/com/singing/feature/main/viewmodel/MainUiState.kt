package com.singing.feature.main.viewmodel

import com.singing.app.domain.model.Publication
import com.singing.app.domain.model.RecentTrack
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.model.UserData
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

data class MainUiState(
    val recentTracks: PersistentList<RecentTrack> = persistentListOf(),
    val records: PersistentList<RecordData> = persistentListOf(),
    val latestPublications: PersistentList<Publication> = persistentListOf(),
    val user: UserData? = null,
)