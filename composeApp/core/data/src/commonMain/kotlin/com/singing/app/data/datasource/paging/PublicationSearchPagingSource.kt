package com.singing.app.data.datasource.paging

import com.singing.app.data.datasource.declaration.PublicationDataSource
import com.singing.app.data.datasource.paging.base.BasePagingSource
import com.singing.app.domain.model.Publication
import com.singing.app.domain.payload.PublicationSearchFilters


class PublicationSearchPagingSource(
    private val filters: PublicationSearchFilters,
    private val dataSource: PublicationDataSource.Remote,
) : BasePagingSource<Publication>(
    dataFetcher = {
        dataSource.search(it, filters)
    },
)
