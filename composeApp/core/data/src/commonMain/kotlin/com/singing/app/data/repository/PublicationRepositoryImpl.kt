package com.singing.app.data.repository

import androidx.paging.PagingData
import com.singing.app.data.datasource.declaration.PublicationDataSource
import com.singing.app.data.repository.base.PagingRepository
import com.singing.app.domain.model.DataState
import com.singing.app.domain.model.Publication
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.repository.PublicationRepository
import com.singing.app.domain.repository.RecordRepository
import kotlinx.coroutines.flow.Flow

class PublicationRepositoryImpl(
    private val recordRepository: RecordRepository,
    private val dataSource: PublicationDataSource.Remote,
) : PublicationRepository, PagingRepository() {
    override suspend fun publishRecord(
        record: RecordData,
        description: String,
        tags: List<String>,
    ): DataState<Publication> {
        if (record.isPublished) throw IllegalArgumentException("Record already published")

        return dataSource.create(
            record.key.remoteId!!,
            description,
            tags,
        ).asDataState.also {
            if (it is DataState.Success) {
                recordRepository.markPublished(it.data.record)
            }
        }
    }

    override suspend fun getRandomPublication(): DataState<Publication> =
        dataSource.fetchRandom().asDataState

    override suspend fun getRecordPublication(record: RecordData): Publication {
        if (!record.isSavedRemote) throw IllegalArgumentException("Record not uploaded")
        if (!record.isPublished) throw IllegalArgumentException("Record not published")

        val result = dataSource.fetchByRecord(record.key.remoteId!!)

        return !result
    }

    override suspend fun getLatestUserPublications(limit: Int): DataState<List<Publication>> =
        dataSource.fetchLatestOwned().asDataState

    override fun getAccountPublications(accountId: Int): Flow<PagingData<Publication>> =
        doPagingRequest { page ->
            dataSource.fetchByUser(page, accountId)
        }

    override suspend fun deletePublication(publication: Publication) {
        dataSource.deletePublication(publication.id)
    }
}
