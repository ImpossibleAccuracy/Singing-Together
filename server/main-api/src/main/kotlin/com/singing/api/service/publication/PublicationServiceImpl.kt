package com.singing.api.service.publication

import com.singing.api.constants.Pagination
import com.singing.api.domain.model.AccountEntity
import com.singing.api.domain.model.PublicationEntity
import com.singing.api.domain.model.PublicationTagEntity
import com.singing.api.domain.model.RecordEntity
import com.singing.api.domain.repository.PublicationRepository
import com.singing.api.domain.repository.PublicationTagRepository
import com.singing.api.domain.repository.pagination.OffsetBasedPageRequest
import com.singing.api.domain.specifications.and
import com.singing.api.domain.specifications.get
import com.singing.api.domain.specifications.join
import com.singing.api.domain.specifications.where
import com.singing.api.security.getAuthentication
import com.singing.domain.model.PublicationSort
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.random.Random.Default.nextLong

@Service
class PublicationServiceImpl(
    private val publicationRepository: PublicationRepository,
    private val publicationTagRepository: PublicationTagRepository,
) : PublicationService {
    override suspend fun get(publicationId: Int): Optional<PublicationEntity> =
        publicationRepository.findById(publicationId)

    override suspend fun isPublished(record: RecordEntity): Boolean =
        publicationRepository.existsByRecord_Id(record.id!!)

    override suspend fun publishRecord(
        record: RecordEntity,
        account: AccountEntity,
        description: String,
        tags: List<String>,
    ): PublicationEntity {
        val actualTags = tags.map {
            publicationTagRepository.findByTitle(it)
                .orElseGet {
                    publicationTagRepository.save(
                        PublicationTagEntity(
                            title = it
                        )
                    )
                }
        }

        return PublicationEntity()
            .apply {
                this.record = record
                this.account = account
                this.description = description
                this.tags = actualTags.toSet()
            }
            .let(publicationRepository::save)
    }

    override suspend fun all(): List<PublicationEntity> =
        publicationRepository.findAll()

    override suspend fun byAccount(
        accountId: Int,
        page: Int,
        sort: PublicationSort
    ): List<PublicationEntity> {
        val sortBy = Sort.by(sort.toOrder())

        return publicationRepository.findByAccount_Id(
            id = accountId,
            pageable = OffsetBasedPageRequest(
                (Pagination.PUBLICATION_PAGE_SIZE * page).toLong(),
                Pagination.PUBLICATION_PAGE_SIZE,
                sort = sortBy,
            )
        )
    }

    override suspend fun search(
        page: Int,
        tags: List<String>?,
        description: String?,
        showOwnPublications: Boolean,
        sort: PublicationSort
    ): List<PublicationEntity> {
        val tagsSearch = tags?.let {
            where<PublicationEntity> {
                it.join(PublicationEntity::tags)
                    .get(PublicationTagEntity::title)
                    .`in`(tags)
            }
        }

        val descriptionSearch =
            description?.let {
                where<PublicationEntity> {
                    like(
                        lower(it.get(PublicationEntity::description)),
                        "%${description.lowercase()}%",
                    )
                }
            }

        val userAccount = getAuthentication()?.account
        val showOwnPublicationsSearch = if (userAccount == null || showOwnPublications) null
        else where<PublicationEntity> {
            it.join(PublicationEntity::account)
                .get(AccountEntity::id)
                .`in`(userAccount.id)
                .not()
        }

        val pagination = OffsetBasedPageRequest(
            (Pagination.PUBLICATION_PAGE_SIZE * page).toLong(),
            Pagination.PUBLICATION_PAGE_SIZE,
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

                if (count == 0L) {
                    return null
                }

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

    override suspend fun delete(publicationId: Int) {
        publicationRepository.deleteById(publicationId)
    }
}
