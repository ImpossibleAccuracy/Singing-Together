package com.singing.app.domain.repository

import androidx.paging.PagingData
import com.singing.app.domain.model.Publication
import com.singing.app.domain.payload.PublicationSearchFilters
import kotlinx.coroutines.flow.Flow

interface PublicationSearchRepository {
    fun loadPublicationsByFilters(filters: PublicationSearchFilters): Flow<PagingData<Publication>>
}
