package com.singing.app.data.repository

import androidx.paging.PagingData
import com.singing.app.data.datasource.declaration.PublicationDataSource
import com.singing.app.data.repository.base.PagingRepository
import com.singing.app.domain.model.DataState
import com.singing.app.domain.model.Publication
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.repository.PublicationRepository
import com.singing.app.domain.repository.RecordRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private val deletedPublications = MutableStateFlow<List<Int>>(listOf())

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
                recordRepository.markPublished(it.data.record, true)
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

    override fun getLatestUserPublications(limit: Int): Flow<DataState<List<Publication>>> =
        flow {
            val result = dataSource.fetchLatestOwned()

            emit(result.asDataState)
        }.combine(deletedPublications) { result, deleted ->
            if (result is DataState.Success) {
                val filtered = result.data.filter {
                    !deleted.contains(it.id)
                }

                DataState.of(filtered)
            } else {
                result
            }
        }

    override fun getAccountPublications(accountId: Int): Flow<PagingData<Publication>> =
        doPagingRequest { page ->
            dataSource.fetchByUser(page, accountId)
        }

    override suspend fun deletePublication(publication: Publication) = coroutineScope {
        // FIXME: Delete request with ktor never completes
        launch { dataSource.deletePublication(publication.id) }

        recordRepository.markPublished(publication.record, false)

        deletedPublications.update { it.plus(publication.id) }
    }
}
