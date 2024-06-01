package com.singing.app.data.datasource.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.singing.app.data.database.AppDatabase
import com.singing.app.data.datamapper.impl.map
import com.singing.app.data.datasource.declaration.RecentTracksDataSource
import com.singing.app.domain.model.RecentTrack
import com.singing.domain.model.AudioFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import pro.respawn.apiresult.ApiResult

class RecentTracksLocalDataSourceImpl(
    private val appDatabase: AppDatabase,
) : RecentTracksDataSource.Local {
    override suspend fun exists(file: AudioFile): Boolean =
        appDatabase.recentTrackQueries
            .existsByPath(file.file.fullPath)
            .executeAsOne()

    override suspend fun create(file: AudioFile) {
        appDatabase.recentTrackQueries.insert(
            createdAt = Clock.System.now().toString(),
            isFavourute = 0,
            path = file.file.fullPath,
            duration = file.duration,
            name = file.name,
        )
    }

    override suspend fun getTrackList(page: Int): ApiResult<List<RecentTrack>> = ApiResult {
        appDatabase.recentTrackQueries.selectAll()
            .executeAsList()
            .map(::map)
    }

    override fun observeRecentTracks(): Flow<List<RecentTrack>> =
        appDatabase.recentTrackQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { items ->
                items
                    .map(::map)
                    .sortedByDescending { it.createdAt.instant }
            }

    override suspend fun updateTrackFavourite(
        track: RecentTrack,
        isFavourite: Boolean
    ): ApiResult<RecentTrack> = ApiResult {
        appDatabase.recentTrackQueries.updateFavouriteState(
            if (isFavourite) 1 else 0,
            track.id.toLong(),
        )

        appDatabase.recentTrackQueries
            .selectById(track.id.toLong())
            .executeAsOne()
            .let(::map)
    }

    override suspend fun delete(track: RecentTrack) {
        appDatabase.recentTrackQueries.deleteById(track.id.toLong())
    }
}
