package com.singing.app.data.datasource.declaration

import com.singing.app.domain.model.RecentTrack
import kotlinx.coroutines.flow.Flow
import pro.respawn.apiresult.ApiResult

sealed interface RecentTracksDataSource {
    interface Local : RecentTracksDataSource {
        suspend fun getTrackList(page: Int): ApiResult<List<RecentTrack>>

        fun observeRecentTracks(): Flow<List<RecentTrack>>

        fun observeFavouriteTracks(): Flow<List<RecentTrack>>

        suspend fun updateTrackFavourite(
            track: RecentTrack,
            isFavourite: Boolean
        ): ApiResult<RecentTrack>
    }
}