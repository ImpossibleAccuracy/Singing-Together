package com.singing.feature.community.domain.usecase

import com.singing.app.domain.model.DataState
import com.singing.app.domain.model.Publication
import com.singing.app.domain.repository.PublicationRepository
import kotlinx.coroutines.flow.Flow

class GetRandomPublicationUseCase(
    private val publicationRepository: PublicationRepository,
) {
    suspend operator fun invoke(): Flow<DataState<Publication>> =
        publicationRepository.getRandomPublication()
}
