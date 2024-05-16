package org.singing.app.domain.repository.publication

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import org.singing.app.domain.model.AccountUiData
import org.singing.app.domain.model.Publication
import org.singing.app.domain.model.RecordData
import org.singing.app.domain.repository.StateRepository
import org.singing.app.domain.repository.record.RecordRepository
import org.singing.app.domain.repository.track.generateString
import org.singing.app.domain.store.account.UserContainer

class PublicationRepository(
    private val recordRepository: RecordRepository,
) : StateRepository<Publication>(DefaultItems) {
    companion object {
        const val PublicationsCount = 2

        private val DefaultItems = RecordRepository.DefaultItems.take(PublicationsCount).map {
            Publication(
                author = AccountUiData(
                    username = "Admin",
                    avatar = null
                ),
                createdAt = it.createdAt,
                description = generateString(50),
                record = it,
            )
        }
    }

    fun getLatestUserPublications(limit: Int): Flow<List<Publication>> {
        return items.map {
            if (it.size > limit) it.subList(0, limit)
            else it
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
            record = record,
        ).also {
            addSingle(it)

            recordRepository.markPublished(record)
        }
    }
}
