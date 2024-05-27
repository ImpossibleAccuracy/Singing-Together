package com.singing.app.domain.usecase

import com.singing.app.domain.model.Publication
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.repository.PublicationRepository

class FindRecordPublicationUseCase(
    private val publicationRepository: PublicationRepository,
) {
    suspend operator fun invoke(record: RecordData): Publication? =
        publicationRepository.getRecordPublication(record)
}
