package com.singing.app.data.repository.base

import androidx.paging.PagingData
import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import com.singing.app.data.Constants
import kotlinx.coroutines.flow.Flow
import pro.respawn.apiresult.ApiResult

abstract class PagingRepository {
    protected fun <Model : Any> doPagingRequest(
        pageSize: Int = Constants.MAX_PAGE_SIZE,
        prefetchDistance: Int = Constants.PREFETCH_DISTANCE,
        enablePlaceholders: Boolean = true,
        initialLoadSize: Int = pageSize * 3,
        maxSize: Int = Int.MAX_VALUE,
        jumpThreshold: Int = Int.MIN_VALUE,
        fetch: suspend (page: Int) -> ApiResult<List<Model>>,
    ): Flow<PagingData<Model>> {
        return Pager(
            config = PagingConfig(
                pageSize,
                prefetchDistance,
                enablePlaceholders,
                initialLoadSize,
                maxSize,
                jumpThreshold
            ),
            pagingSourceFactory = { BasePagingSource(fetch) }
        ).flow
    }
}