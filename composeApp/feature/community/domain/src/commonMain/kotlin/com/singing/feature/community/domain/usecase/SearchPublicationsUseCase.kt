package com.singing.feature.community.domain.usecase

import com.singing.app.domain.payload.PublicationSearchFilters
import com.singing.app.domain.repository.PublicationSearchRepository

class SearchPublicationsUseCase(
    private val searchRepository: PublicationSearchRepository,
) {
    operator fun invoke(filters: PublicationSearchFilters) = searchRepository
        .loadPublicationsByFilters(filters)
}