package com.singing.app.data.repository

import androidx.paging.PagingData
import com.singing.app.domain.model.Publication
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.model.UserData
import com.singing.app.domain.provider.UserProvider
import com.singing.app.domain.repository.PublicationRepository
import com.singing.app.domain.repository.RecordRepository
import kotlinx.coroutines.flow.Flow

class PublicationRepositoryImpl(
    private val userProvider: UserProvider,
    private val recordRepository: RecordRepository,
) : PublicationRepository, StateRepository<Publication>() {
    override suspend fun publishRecord(
        account: UserData,
        record: RecordData,
        description: String
    ): Publication = TODO()

    override suspend fun getRandomPublication(): Publication = TODO()

    override suspend fun getRecordPublication(record: RecordData): Publication? = TODO()

    override fun getLatestUserPublications(limit: Int): Flow<List<Publication>> = TODO()

    override fun getAccountPublications(accountId: Int): Flow<PagingData<Publication>> = TODO()
}
