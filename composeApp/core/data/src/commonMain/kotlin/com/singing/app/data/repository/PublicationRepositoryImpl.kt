package com.singing.app.data.repository

import androidx.paging.PagingData
import com.singing.app.data.datasource.declaration.PublicationDataSource
import com.singing.app.domain.model.DataState
import com.singing.app.domain.model.Publication
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.repository.PublicationRepository
import com.singing.app.domain.repository.RecordRepository
import kotlinx.coroutines.flow.Flow
import pro.respawn.apiresult.ApiResult

class PublicationRepositoryImpl(
    private val recordRepository: RecordRepository,
    private val dataSource: PublicationDataSource.Remote,
) : PublicationRepository {
    override suspend fun publishRecord(
        record: RecordData,
        description: String
    ): DataState<Publication> {
        if (record.isPublished) throw IllegalArgumentException("Record already published")

        return dataSource.create(
            record.key.remoteId!!,
            description,
            listOf() // TODO
        ).asDataState.also {
            if (it is DataState.Success) {
                recordRepository.markPublished(it.data.record)
            }
        }
    }

    override suspend fun getRandomPublication(): DataState<Publication> =
        dataSource.fetchRandom().asDataState

    override suspend fun getRecordPublication(record: RecordData): Publication? {
        if (!record.isSavedRemote || !record.isPublished) throw IllegalArgumentException("Record not published")

        return when (val result = dataSource.fetchByRecord(record.key.remoteId!!)) {
            is ApiResult.Success -> result.result
            else -> null
        }
    }

    override suspend fun getLatestUserPublications(limit: Int): DataState<List<Publication>> =
        dataSource.fetchLatestOwned().asDataState

    override fun getAccountPublications(accountId: Int): Flow<PagingData<Publication>> = TODO()
}
