package com.singing.feature.community.domain.usecase

import com.singing.app.domain.model.DataState
import com.singing.app.domain.model.Publication
import com.singing.app.domain.repository.PublicationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetRandomPublicationUseCase(
    private val publicationRepository: PublicationRepository,
) {
    suspend operator fun invoke(): Flow<DataState<Publication>> =
        flow {
            emit(DataState.Loading)

            emit(publicationRepository.getRandomPublication())
        }
}
