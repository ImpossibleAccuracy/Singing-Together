package com.singing.app.data.datasource.impl

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.singing.app.data.database.AppDatabase
import com.singing.app.data.datasource.declaration.RecentTracksDataSource
import com.singing.app.data.datasource.utils.DataMapper
import com.singing.app.data.sqldelight.record.RecentTrackEntity
import com.singing.app.domain.model.RecentTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pro.respawn.apiresult.ApiResult

class RecentTracksLocalDataSourceImpl(
    private val appDatabase: AppDatabase,
    private val entityDataMapper: DataMapper<RecentTrackEntity, RecentTrack>,
) : RecentTracksDataSource.Local {
    override suspend fun getTrackList(page: Int): ApiResult<List<RecentTrack>> = ApiResult {
        appDatabase.recentTrackQueries.selectAll()
            .executeAsList()
            .map(entityDataMapper::map)
    }

    override fun observeRecentTracks(): Flow<List<RecentTrack>> =
        appDatabase.recentTrackQueries.selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { items ->
                items
                    .map(entityDataMapper::map)
                    .sortedByDescending { it.createdAt.instant }
            }

    override fun observeFavouriteTracks(): Flow<List<RecentTrack>> =
        appDatabase.recentTrackQueries.selectFavourites()
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { items ->
                items
                    .map(entityDataMapper::map)
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

        val entity = appDatabase.recentTrackQueries.selectById(track.id.toLong())

        entityDataMapper.map(entity.executeAsOne())
    }
}
