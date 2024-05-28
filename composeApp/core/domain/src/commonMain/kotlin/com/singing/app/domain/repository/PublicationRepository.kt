package com.singing.app.domain.repository

import androidx.paging.PagingData
import com.singing.app.domain.model.DataState
import com.singing.app.domain.model.Publication
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.model.UserData
import kotlinx.coroutines.flow.Flow

interface PublicationRepository {
    suspend fun publishRecord(
        account: UserData,
        record: RecordData,
        description: String,
    ): Publication

    suspend fun getRandomPublication(): Flow<DataState<Publication>>

    suspend fun getRecordPublication(record: RecordData): Publication?

    fun getLatestUserPublications(limit: Int): Flow<List<Publication>>

    fun getAccountPublications(accountId: Int): Flow<PagingData<Publication>>
}
