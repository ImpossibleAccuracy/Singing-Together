package com.singing.app.domain.repository

import androidx.paging.PagingData
import com.singing.app.domain.model.RecentTrack
import kotlinx.coroutines.flow.Flow

interface RecentTrackRepository {
    fun getTracks(): Flow<PagingData<RecentTrack>>

    fun getRecentTracks(limit: Int): Flow<List<RecentTrack>>

    fun getFavouriteTracks(limit: Int): Flow<List<RecentTrack>>

    suspend fun updateTrackFavourite(track: RecentTrack, isFavourite: Boolean)
}