package com.singing.app.data.repository

import androidx.paging.PagingData
import com.singing.app.base.exists
import com.singing.app.data.datasource.declaration.RecentTracksDataSource
import com.singing.app.data.repository.base.PagingRepository
import com.singing.app.domain.model.RecentTrack
import com.singing.app.domain.repository.RecentTrackRepository
import com.singing.domain.model.AudioFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class RecentTrackRepositoryImpl(
    private val dataSource: RecentTracksDataSource.Local,
) : RecentTrackRepository, PagingRepository() {
    override suspend fun addRecentTrack(file: AudioFile) {
        if (!dataSource.exists(file)) {
            dataSource.create(file)
        }
    }

    override fun getTracks(): Flow<PagingData<RecentTrack>> =
        doPagingRequest { page ->
            dataSource.getTrackList(page)
        }

    private fun getRecentTracks(): Flow<List<RecentTrack>> =
        dataSource.observeRecentTracks()
            .map { items ->
                items.filter {
                    it.audioFile.file.exists.also { exists ->
                        if (!exists) {
                            dataSource.delete(it)
                        }
                    }
                }
            }

    override fun getRecentTracks(limit: Int): Flow<List<RecentTrack>> =
        getRecentTracks()
            .map {
                if (it.size > limit) it.subList(0, limit)
                else it
            }
            .distinctUntilChanged()

    override fun getFavouriteTracks(limit: Int): Flow<List<RecentTrack>> =
        getRecentTracks()
            .map { items ->
                items.filter { it.isFavourite }
            }
            .map {
                if (it.size > limit) it.subList(0, limit)
                else it
            }
            .distinctUntilChanged()

    override suspend fun updateTrackFavourite(track: RecentTrack, isFavourite: Boolean) {
        if (track.isFavourite == isFavourite) return

        dataSource.updateTrackFavourite(track, isFavourite)
    }
}
