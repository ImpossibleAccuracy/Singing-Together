package com.singing.feature.main.domain.usecase

import com.singing.app.domain.model.Publication
import com.singing.app.domain.repository.PublicationRepository
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetRecentPublicationsUseCase(
    private val publicationRepository: PublicationRepository,
) {
    companion object {
        const val DEFAULT_LIMIT = 5
    }

    operator fun invoke(limit: Int = DEFAULT_LIMIT): Flow<PersistentList<Publication>> =
        publicationRepository.getLatestUserPublications(limit)
            .map { it.toPersistentList() }
}