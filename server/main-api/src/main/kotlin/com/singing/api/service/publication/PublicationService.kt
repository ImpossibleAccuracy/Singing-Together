package com.singing.api.service.publication

import com.singing.api.domain.model.AccountEntity
import com.singing.api.domain.model.PublicationEntity
import com.singing.api.domain.model.RecordEntity
import com.singing.domain.model.PublicationSort
import java.util.*

interface PublicationService {
    suspend fun get(publicationId: Int): Optional<PublicationEntity>

    suspend fun isPublished(record: RecordEntity): Boolean

    suspend fun publishRecord(
        record: RecordEntity,
        account: AccountEntity,
        description: String,
    ): PublicationEntity

    suspend fun all(): List<PublicationEntity>

    suspend fun byAccount(accountId: Int, page: Int, sort: PublicationSort): List<PublicationEntity>

    suspend fun search(
        page: Int,
        tags: List<String>?,
        description: String?,
        showOwnPublications: Boolean,
        sort: PublicationSort,
    ): List<PublicationEntity>

    suspend fun byRecord(recordId: Int): Optional<PublicationEntity>

    suspend fun random(period: Long?): PublicationEntity?

    suspend fun delete(publicationId: Int)
}
