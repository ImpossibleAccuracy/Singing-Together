package com.singing.app.domain.usecase

import com.singing.app.domain.model.Publication
import com.singing.app.domain.repository.PublicationRepository

class DeletePublicationUseCase(
    private val publicationRepository: PublicationRepository,
) {
    suspend operator fun invoke(publication: Publication) {
        publicationRepository.deletePublication(publication)
    }
}