package com.singing.app.data.repository

import androidx.paging.PagingData
import com.singing.app.data.datasource.declaration.PublicationDataSource
import com.singing.app.data.repository.base.PagingRepository
import com.singing.app.domain.model.Publication
import com.singing.app.domain.payload.PublicationSearchFilters
import com.singing.app.domain.repository.PublicationSearchRepository
import kotlinx.coroutines.flow.Flow

class PublicationSearchRepositoryImpl(
    private val dataSource: PublicationDataSource.Remote,
) : PublicationSearchRepository, PagingRepository() {
    override fun loadPublicationsByFilters(filters: PublicationSearchFilters): Flow<PagingData<Publication>> =
        doPagingRequest { page ->
            dataSource.search(page, filters)
        }
}
