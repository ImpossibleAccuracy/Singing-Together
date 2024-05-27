package com.singing.app.data.repository

import androidx.paging.PagingData
import com.singing.app.domain.model.RecentTrack
import com.singing.app.domain.repository.RecentTrackRepository
import kotlinx.coroutines.flow.Flow

class RecentTrackRepositoryImpl : RecentTrackRepository, StateRepository<RecentTrack>() {
    override fun getTracks(): Flow<PagingData<RecentTrack>> = TODO()

    override fun getRecentTracks(limit: Int): Flow<List<RecentTrack>> = TODO()

    override fun getFavouriteTracks(limit: Int): Flow<List<RecentTrack>> = TODO()

    override suspend fun updateTrackFavourite(track: RecentTrack, isFavourite: Boolean) = TODO()
}
