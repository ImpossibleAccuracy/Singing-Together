package org.singing.app.domain.usecase

import org.singing.app.domain.model.Publication
import org.singing.app.domain.model.RecordData
import org.singing.app.domain.repository.publication.PublicationRepository

class FindRecordPublicationUseCase(
    private val publicationRepository: PublicationRepository,
) {
    suspend operator fun invoke(record: RecordData): Publication =
        publicationRepository.getRecordPublication(record)
}
