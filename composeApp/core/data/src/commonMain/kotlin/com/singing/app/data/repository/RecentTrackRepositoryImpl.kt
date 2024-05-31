package com.singing.app.data.repository

import androidx.paging.PagingData
import com.singing.app.data.datasource.declaration.RecentTracksDataSource
import com.singing.app.data.repository.base.PagingRepository
import com.singing.app.domain.model.RecentTrack
import com.singing.app.domain.repository.RecentTrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RecentTrackRepositoryImpl(
    private val dataSource: RecentTracksDataSource.Local,
) : RecentTrackRepository, PagingRepository() {
    override fun getTracks(): Flow<PagingData<RecentTrack>> =
        doPagingRequest { page ->
            dataSource.getTrackList(page)
        }

    override fun getRecentTracks(limit: Int): Flow<List<RecentTrack>> =
        dataSource.observeRecentTracks()
            .map {
                if (it.size > limit) it.subList(0, limit)
                else it
            }

    override fun getFavouriteTracks(limit: Int): Flow<List<RecentTrack>> =
        dataSource.observeFavouriteTracks()
            .map {
                if (it.size > limit) it.subList(0, limit)
                else it
            }

    override suspend fun updateTrackFavourite(track: RecentTrack, isFavourite: Boolean) {
        if (track.isFavourite == isFavourite) return

        dataSource.updateTrackFavourite(track, isFavourite)
    }
}
