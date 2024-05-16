package org.singing.app.ui.screens.main

import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.singing.app.domain.model.RecentTrack
import org.singing.app.domain.model.RecordData
import org.singing.app.domain.repository.publication.PublicationRepository
import org.singing.app.domain.repository.record.RecordRepository
import org.singing.app.domain.repository.track.RecentTrackRepository
import org.singing.app.domain.store.account.UserContainer
import org.singing.app.ui.base.AppViewModel

class MainViewModel(
    private val recordRepository: RecordRepository,
    private val recentTrackRepository: RecentTrackRepository,
    private val publicationRepository: PublicationRepository,
) : AppViewModel() {
    val records = recordRepository
        .getRecords()
        .stateIn()

    val latestPublications = publicationRepository
        .getLatestUserPublications(limit = 3)
        .stateIn()

    val recentTracks = recentTrackRepository
        .getRecentTracks()
        .stateIn()

    private val _user = UserContainer.user
    val user = _user.asStateFlow()

    fun deleteRecord(record: RecordData) =
        viewModelScope.launch {
            recordRepository.deleteRecord(record)
        }

    fun updateRecentTrackFavourite(track: RecentTrack, isFavourite: Boolean) =
        viewModelScope.launch {
            recentTrackRepository.updateRecentTrackFavourite(track, isFavourite)
        }
}
