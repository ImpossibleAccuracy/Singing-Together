package com.singing.app.data.datasource.declaration

import com.singing.app.domain.model.Publication
import com.singing.app.domain.model.PublicationTagStatistics
import com.singing.app.domain.payload.PublicationSearchFilters
import pro.respawn.apiresult.ApiResult

sealed interface PublicationDataSource {
    interface Remote : PublicationDataSource {
        suspend fun create(
            recordId: Int,
            description: String,
            tags: List<String>,
        ): ApiResult<Publication>

        suspend fun search(
            page: Int,
            filters: PublicationSearchFilters,
        ): ApiResult<List<Publication>>

        suspend fun fetchLatestOwned(): ApiResult<List<Publication>>

        suspend fun fetchByUser(page: Int, userId: Int): ApiResult<List<Publication>>

        suspend fun fetchRandom(): ApiResult<Publication>

        suspend fun fetchByRecord(recordId: Int): ApiResult<Publication>

        suspend fun fetchPopularTags(): ApiResult<List<PublicationTagStatistics>>

        suspend fun deletePublication(publicationId: Int)
    }
}
