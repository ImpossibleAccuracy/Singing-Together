package com.singing.app.data.repository.base

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import com.singing.app.data.Constants
import com.singing.app.data.datasource.paging.base.BasePagingSource
import kotlinx.coroutines.flow.Flow

abstract class BaseRepository {
    protected fun <Model : Any> doPagingRequest(
        pagingSource: BasePagingSource<Model>,
        pageSize: Int = Constants.MAX_PAGE_SIZE,
        prefetchDistance: Int = Constants.PREFETCH_DISTANCE,
        enablePlaceholders: Boolean = true,
        initialLoadSize: Int = pageSize * 3,
        maxSize: Int = Int.MAX_VALUE,
        jumpThreshold: Int = Int.MIN_VALUE
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
            pagingSourceFactory = {
                pagingSource
            }
        ).flow
    }
}