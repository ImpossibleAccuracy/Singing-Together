package com.singing.app.data.datasource.declaration

import com.singing.app.domain.model.Publication
import com.singing.app.domain.payload.PublicationSearchFilters
import pro.respawn.apiresult.ApiResult

interface RemotePublicationDataSource {
    suspend fun search(
        page: Int,
        filters: PublicationSearchFilters,
    ): ApiResult<List<Publication>>
}
