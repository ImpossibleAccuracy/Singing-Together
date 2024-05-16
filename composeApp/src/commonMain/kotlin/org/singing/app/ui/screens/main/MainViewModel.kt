package org.singing.app.ui.screens.main

import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.singing.app.domain.model.Publication
import org.singing.app.domain.model.RecentTrack
import org.singing.app.domain.model.RecordData
import org.singing.app.domain.repository.publication.PublicationRepository
import org.singing.app.domain.player.RecordPlayer
import org.singing.app.domain.repository.record.RecordRepository
import org.singing.app.domain.repository.track.RecentTrackRepository
import org.singing.app.domain.store.account.UserContainer
import org.singing.app.domain.usecase.DeleteRecordUseCase
import org.singing.app.domain.usecase.FindRecordPublicationUseCase
import org.singing.app.domain.usecase.PublishRecordUseCase
import org.singing.app.domain.usecase.UploadRecordUseCase
import org.singing.app.ui.base.AppViewModel

class MainViewModel(
    private val deleteRecordUseCase: DeleteRecordUseCase,
    private val publishRecordUseCase: PublishRecordUseCase,
    private val uploadRecordUseCase: UploadRecordUseCase,
    private val findRecordPublicationUseCase: FindRecordPublicationUseCase,
    publicationRepository: PublicationRepository,
    recordRepository: RecordRepository,
    private val recentTrackRepository: RecentTrackRepository,
    val recordPlayer: RecordPlayer,
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


    override fun onDispose() {
        resetRecordPlayer()

        super.onDispose()
    }

    fun resetRecordPlayer() {
        viewModelScope.launch {
            recordPlayer.reset()
        }
    }


    fun updateRecentTrackFavourite(track: RecentTrack, isFavourite: Boolean) =
        viewModelScope.launch {
            recentTrackRepository.updateRecentTrackFavourite(track, isFavourite)
        }

    suspend fun getRecordPublication(record: RecordData): Publication =
        findRecordPublicationUseCase(record)

    fun uploadRecord(record: RecordData) =
        viewModelScope.launch {
            uploadRecordUseCase(record)
        }

    fun publishRecord(record: RecordData, description: String) =
        viewModelScope.launch {
            publishRecordUseCase(record, description)
        }

    fun deleteRecord(record: RecordData) =
        viewModelScope.launch {
            deleteRecordUseCase(record)
        }
}
