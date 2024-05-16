package org.singing.app.domain.usecase

import org.singing.app.domain.model.Publication
import org.singing.app.domain.model.RecordData
import org.singing.app.domain.repository.publication.PublicationRepository
import org.singing.app.domain.repository.record.RecordRepository

class PublishRecordUseCase(
    private val recordRepository: RecordRepository,
    private val publicationRepository: PublicationRepository,
) {
    suspend operator fun invoke(record: RecordData, description: String): Publication {
        val actualRecord = when (record.isSavedRemote) {
            true -> record
            false -> recordRepository.uploadRecord(record)
        }

        return publicationRepository.publishRecord(actualRecord, description)
    }
}
