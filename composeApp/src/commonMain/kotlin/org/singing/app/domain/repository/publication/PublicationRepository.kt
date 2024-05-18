package org.singing.app.domain.repository.publication

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import org.singing.app.domain.model.CategoryInfo
import org.singing.app.domain.model.Publication
import org.singing.app.domain.model.PublicationSort
import org.singing.app.domain.model.RecordData
import org.singing.app.domain.model.stable.StableInstant
import org.singing.app.domain.repository.StateRepository
import org.singing.app.domain.repository.account.AccountRepository
import org.singing.app.domain.repository.record.RecordRepository
import org.singing.app.domain.repository.track.generateString
import org.singing.app.domain.store.account.UserContainer

class PublicationRepository(
    private val recordRepository: RecordRepository,
) : StateRepository<Publication>(DefaultItems) {
    companion object {
        const val PublicationsCount = 4
        const val PageSize = 2

        val DefaultItems = RecordRepository.DefaultItems.take(PublicationsCount).mapIndexed { index, item ->
            Publication(
                author = AccountRepository.DefaultItems[index % AccountRepository.DefaultItems.size],
                createdAt = StableInstant(item.createdAt.instant),
                description = generateString(100),
                record = item,
            )
        }
    }

    suspend fun publishRecord(
        record: RecordData,
        description: String,
    ): Publication = withContext(Dispatchers.IO) {
        Publication(
            author = UserContainer.user.value!!,
            createdAt = StableInstant(Clock.System.now()),
            description = description,
            record = recordRepository.markPublished(record),
        ).also {
            addSingle(it)
        }
    }

    fun getLatestUserPublications(limit: Int): Flow<List<Publication>> = items
        .map { list ->
            list.filter { it.author.id == UserContainer.user.value!!.id }
                .sortedByDescending { it.createdAt.instant }
        }
        .map {
            if (it.size > limit) it.subList(0, limit)
            else it
        }

    suspend fun loadPublicationsByFilters(
        page: Int,
        tags: List<String>?,
        description: String?,
        showOwnPublications: Boolean,
        sort: PublicationSort,
    ): List<Publication> = withContext(Dispatchers.IO) {
        println("loadPublicationsByFilters()")

        val currentUser = UserContainer.user.value

        val items = items.value
            .filter { item ->
                val descriptionCheck = if (description != null) {
                    item.description.contains(description, true)
                } else true

                val showOwnPublicationCheck = if (currentUser != null && !showOwnPublications) {
                    item.author.id != currentUser.id
                } else true

                descriptionCheck && showOwnPublicationCheck
            }

        val sortedItems =
            when (sort) {
                PublicationSort.DateCreated -> items.sortedBy { it.createdAt.instant }

                PublicationSort.Accuracy -> items.sortedBy {
                    if (it.record is RecordData.Cover) it.record.accuracy
                    else null
                }

                PublicationSort.Duration -> items.sortedBy { it.record.duration }
            }

        val startOffset = page * PageSize
        val endOffset = (page + 1) * PageSize

        if (sortedItems.size < startOffset) listOf()
        else if (sortedItems.size < endOffset) sortedItems.subList(startOffset, sortedItems.size)
        else sortedItems.subList(startOffset, endOffset)
    }

    suspend fun getAccountPublications(accountId: Int): List<Publication> = items
        .map { list ->
            list.filter { it.author.id == accountId }
                .sortedByDescending { it.createdAt.instant }
        }
        .first()

    suspend fun getRecordPublication(record: RecordData) =
        withContext(Dispatchers.IO) {
            items.value.first {
                it.record == record
            }
        }

    suspend fun getRandomPublication(): Publication =
        withContext(Dispatchers.IO) {
            items.value.random()
        }

    suspend fun getPopularCategories(): List<CategoryInfo> =
        withContext(Dispatchers.IO) {
            listOf(
                CategoryInfo(
                    title = "Pop",
                    publications = 231,
                ),
                CategoryInfo(
                    title = "Rock'n'Roll",
                    publications = 123,
                ),
                CategoryInfo(
                    title = "Rock'n'Roll 2",
                    publications = 456,
                ),
                CategoryInfo(
                    title = "Rock'n'Roll 3",
                    publications = 789,
                ),
                CategoryInfo(
                    title = "Rock'n'Roll 4",
                    publications = 1000,
                ),
            ).sortedByDescending { it.publications }
        }
}
