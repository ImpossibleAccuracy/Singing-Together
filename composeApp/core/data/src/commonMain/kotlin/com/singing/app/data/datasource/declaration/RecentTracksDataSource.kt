package com.singing.app.data.datasource.declaration

import com.singing.app.domain.model.RecentTrack
import com.singing.domain.model.AudioFile
import kotlinx.coroutines.flow.Flow
import pro.respawn.apiresult.ApiResult

sealed interface RecentTracksDataSource {
    interface Local : RecentTracksDataSource {
        suspend fun exists(file: AudioFile): Boolean

        suspend fun create(file: AudioFile)

        suspend fun getTrackList(page: Int): ApiResult<List<RecentTrack>>

        fun observeRecentTracks(): Flow<List<RecentTrack>>

        fun observeFavouriteTracks(): Flow<List<RecentTrack>>

        suspend fun updateTrackFavourite(
            track: RecentTrack,
            isFavourite: Boolean
        ): ApiResult<RecentTrack>

        suspend fun delete(track: RecentTrack)
    }
}