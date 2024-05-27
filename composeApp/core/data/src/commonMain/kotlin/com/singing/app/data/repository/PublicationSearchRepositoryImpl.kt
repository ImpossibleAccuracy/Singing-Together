package com.singing.app.data.repository

import androidx.paging.PagingData
import com.singing.app.data.datasource.declaration.RemotePublicationDataSource
import com.singing.app.data.datasource.paging.PublicationSearchPagingSource
import com.singing.app.data.repository.base.BaseRepository
import com.singing.app.domain.model.Publication
import com.singing.app.domain.payload.PublicationSearchFilters
import com.singing.app.domain.repository.PublicationSearchRepository
import kotlinx.coroutines.flow.Flow

class PublicationSearchRepositoryImpl(
    private val remotePublicationDataSource: RemotePublicationDataSource,
) : PublicationSearchRepository, BaseRepository() {
    override fun loadPublicationsByFilters(filters: PublicationSearchFilters): Flow<PagingData<Publication>> =
        doPagingRequest(
            pagingSource = PublicationSearchPagingSource(filters, remotePublicationDataSource)
        )
}
