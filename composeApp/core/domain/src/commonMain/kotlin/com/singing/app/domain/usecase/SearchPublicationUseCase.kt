package com.singing.app.domain.usecase

import androidx.paging.PagingData
import com.singing.app.domain.model.Publication
import com.singing.app.domain.payload.PublicationSearchFilters
import com.singing.app.domain.repository.PublicationSearchRepository
import kotlinx.coroutines.flow.Flow

class SearchPublicationUseCase(
    private val publicationSearchRepository: PublicationSearchRepository,
) {
    operator fun invoke(filters: PublicationSearchFilters): Flow<PagingData<Publication>> =
        publicationSearchRepository.loadPublicationsByFilters(filters)
}
