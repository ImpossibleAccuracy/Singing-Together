package org.singing.app.domain.repository.publication

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import org.singing.app.domain.model.Publication
import org.singing.app.domain.model.RecordData
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

        val DefaultItems = RecordRepository.DefaultItems.take(PublicationsCount).mapIndexed { index, item ->
            Publication(
                author = AccountRepository.DefaultItems[index % AccountRepository.DefaultItems.size],
                createdAt = item.createdAt,
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
            createdAt = Clock.System.now(),
            description = description,
            record = recordRepository.markPublished(record),
        ).also {
            addSingle(it)
        }
    }

    fun getLatestUserPublications(limit: Int): Flow<List<Publication>> = items
        .map { list ->
            list.filter { it.author.id == UserContainer.user.value!!.id }
                .sortedByDescending { it.createdAt }
        }
        .map {
            if (it.size > limit) it.subList(0, limit)
            else it
        }

    suspend fun getAccountPublications(accountId: Int): List<Publication> = items
        .map { list ->
            list.filter { it.author.id == accountId }
                .sortedByDescending { it.createdAt }
        }
        .first()

    suspend fun getRecordPublication(record: RecordData) = withContext(Dispatchers.IO) {
        items.value.first {
            it.record == record
        }
    }
}
