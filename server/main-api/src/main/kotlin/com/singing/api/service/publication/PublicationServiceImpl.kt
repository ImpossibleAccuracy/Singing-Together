package com.singing.api.service.publication

import com.singing.api.constants.Pagination
import com.singing.api.domain.model.AccountEntity
import com.singing.api.domain.model.PublicationEntity
import com.singing.api.domain.model.RecordEntity
import com.singing.api.domain.repository.PublicationRepository
import com.singing.api.domain.repository.pagination.OffsetBasedPageRequest
import com.singing.api.domain.specifications.and
import com.singing.api.domain.specifications.get
import com.singing.api.domain.specifications.`in`
import com.singing.api.domain.specifications.join
import com.singing.api.domain.specifications.like
import com.singing.api.domain.specifications.where
import com.singing.api.security.getAuthentication
import com.singing.domain.model.PublicationSort
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Optional
import kotlin.random.Random.Default.nextLong

@Service
class PublicationServiceImpl(
    private val publicationRepository: PublicationRepository
) : PublicationService {
    override suspend fun isPublished(record: RecordEntity): Boolean =
        publicationRepository.existsByRecord_Id(record.id!!)

    override suspend fun publishRecord(
        record: RecordEntity,
        account: AccountEntity,
        description: String
    ): PublicationEntity =
        PublicationEntity(
            record = record,
            account = account,
            description = description,
        ).let(publicationRepository::save)

    override suspend fun all(): List<PublicationEntity> =
        publicationRepository.findAll()

    override suspend fun byAccount(
        accountId: Int,
        page: Int,
        sort: PublicationSort
    ): List<PublicationEntity> {
        TODO()
        /*val sortBy = Sort.by(sort.toOrder())

        return publicationRepository.findByAccount_Id(
            id = accountId,
            sort = sortBy,
        )*/
    }

    override suspend fun search(
        page: Int,
        tags: List<String>?,
        description: String?,
        showOwnPublications: Boolean,
        sort: PublicationSort
    ): List<PublicationEntity> {
        val tagsSearch = tags?.let { PublicationEntity::tags.`in`(tags) }

        val descriptionSearch =
            description?.let { PublicationEntity::description.like(description) }

        val userAccount = getAuthentication()?.account
        val showOwnPublicationsSearch = if (userAccount == null || showOwnPublications) null
        else where<PublicationEntity> {
            it.join(PublicationEntity::account)
                .get(AccountEntity::id)
                .`in`(userAccount.id)
                .not()
        }

        val pagination = OffsetBasedPageRequest(
            (Pagination.PAGE_SIZE * page).toLong(),
            Pagination.PAGE_SIZE,
            Sort.by(sort.toOrder())
        )

        return publicationRepository
            .findAll(
                and(tagsSearch, descriptionSearch, showOwnPublicationsSearch),
                pagination,
            )
            .toList()
    }

    override suspend fun byRecord(recordId: Int): Optional<PublicationEntity> =
        publicationRepository.findByRecord_Id(recordId)

    override suspend fun random(period: Long?): PublicationEntity? {
        return when (period) {
            null -> {
                val count = publicationRepository.count()

                val random = nextLong(0, count)

                val list = publicationRepository
                    .findAll(
                        OffsetBasedPageRequest(
                            offset = random,
                            limit = 1,
                            sort = Sort.by(Sort.Direction.DESC, PublicationEntity::createdAt.name)
                        )
                    )

                list.firstOrNull()
            }

            else -> {
                val time = Instant.now().minus(period, ChronoUnit.DAYS)

                val count = publicationRepository.countByCreatedAtGreaterThanEqual(time)

                if (count == 0L) {
                    return null
                }

                val random = nextLong(0, count)

                val list =
                    publicationRepository
                        .findByCreatedAtGreaterThanEqual(
                            time,
                            OffsetBasedPageRequest(
                                offset = random,
                                limit = 1,
                                sort = Sort.by(
                                    Sort.Direction.DESC,
                                    PublicationEntity::createdAt.name
                                )
                            )
                        )

                list.firstOrNull()
            }
        }
    }
}
