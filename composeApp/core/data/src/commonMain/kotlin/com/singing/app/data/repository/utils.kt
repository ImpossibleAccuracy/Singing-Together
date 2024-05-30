package com.singing.app.data.repository

import com.singing.app.domain.model.DataState
import pro.respawn.apiresult.ApiResult
import pro.respawn.apiresult.message

val <T> ApiResult<T>.asDataState: DataState<T>
    get() = when (this) {
        is ApiResult.Error -> DataState.Error(message ?: "Unknown error", e)
        is ApiResult.Success -> DataState.of(result)
        else -> throw IllegalStateException()
    }
