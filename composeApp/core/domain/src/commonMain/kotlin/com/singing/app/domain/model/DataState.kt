package com.singing.app.domain.model

sealed interface DataState<out T> {
    companion object {
        fun <T> of(value: T?): DataState<T> =
            if (value == null) Empty
            else Success(value)
    }

    data class Error(
        val message: String,
        val throwable: Throwable?
    ) : DataState<Nothing>

    data class Success<T>(val data: T) : DataState<T>

    data object Empty : DataState<Nothing>

    data object Loading : DataState<Nothing>
}

fun <T> DataState<T>.valueOrNull(): T? = when (this) {
    is DataState.Success -> data
    else -> null
}

fun <T, R> DataState<T>.mapData(mapper: (T) -> R): DataState<R> =
    when (this) {
        is DataState.Success -> {
            DataState.Success(mapper(this.data))
        }

        else -> {
            @Suppress("UNCHECKED_CAST")
            this as DataState<R>
        }
    }
