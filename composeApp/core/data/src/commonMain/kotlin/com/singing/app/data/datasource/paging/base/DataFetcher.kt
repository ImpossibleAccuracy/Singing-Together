package com.singing.app.data.datasource.paging.base

import pro.respawn.apiresult.ApiResult

fun interface DataFetcher<R> {
    suspend fun fetch(page: Int): ApiResult<List<R>>
}