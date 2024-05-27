package com.singing.app.data.datasource.paging.base

import androidx.paging.PagingSource
import app.cash.paging.PagingState
import pro.respawn.apiresult.fold

abstract class BasePagingSource<Model : Any, DTO : Any>(
    private val dataFetcher: DataFetcher<DTO>,
    private val dataMapper: DataMapper<DTO, Model>,
    private val baseStartingPageIndex: Int = 0,
) : PagingSource<Int, Model>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Model> {
        val position = params.key ?: baseStartingPageIndex

        return dataFetcher.fetch(position)
            .fold(
                onSuccess = {
                    LoadResult.Page(
                        data = it.map(dataMapper::map),
                        prevKey = if (position == 1) null else position - 1,
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