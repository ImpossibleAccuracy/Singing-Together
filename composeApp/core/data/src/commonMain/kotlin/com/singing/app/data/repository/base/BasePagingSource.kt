package com.singing.app.data.repository.base

import androidx.paging.PagingSource
import app.cash.paging.PagingState
import pro.respawn.apiresult.ApiResult
import pro.respawn.apiresult.fold

class BasePagingSource<Model : Any>(
    private val fetch: suspend (page: Int) -> ApiResult<List<Model>>,
    private val baseStartingPageIndex: Int = 0,
) : PagingSource<Int, Model>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Model> {
        val position = params.key ?: baseStartingPageIndex

        return fetch(position)
            .fold(
                onSuccess = {
                    LoadResult.Page(
                        data = it,
                        prevKey = if (position == baseStartingPageIndex) null else position - 1,
                        nextKey = if (it.isEmpty()) null else position + 1
                    )
                },
                onError = {
                    LoadResult.Error(it)
                }
            )
    }

    override fun getRefreshKey(state: PagingState<Int, Model>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}