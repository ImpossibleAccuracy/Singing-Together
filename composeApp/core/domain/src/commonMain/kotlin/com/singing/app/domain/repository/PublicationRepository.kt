package com.singing.app.domain.repository

import androidx.paging.PagingData
import com.singing.app.domain.model.DataState
import com.singing.app.domain.model.Publication
import com.singing.app.domain.model.RecordData
import kotlinx.coroutines.flow.Flow

interface PublicationRepository {
    suspend fun publishRecord(
        record: RecordData,
        description: String,
        tags: List<String>,
    ): DataState<Publication>

    suspend fun getRandomPublication(): DataState<Publication>

    suspend fun getRecordPublication(record: RecordData): Publication?

    suspend fun getLatestUserPublications(limit: Int): DataState<List<Publication>>

    fun getAccountPublications(accountId: Int): Flow<PagingData<Publication>>
}
