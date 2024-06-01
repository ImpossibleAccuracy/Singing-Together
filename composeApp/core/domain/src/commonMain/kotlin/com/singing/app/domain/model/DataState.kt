package com.singing.app.domain.model

sealed interface DataState<out T> {
    companion object {
        fun <T> of(value: T?): DataState<T> =
            if (value == null || (value is List<Any?> && value.isEmpty())) Empty
            else Success(value)
    }

    data class Error(
        val message: String,
        val throwable: Throwable? = null,
    ) : DataState<Nothing>

    data class Success<T>(val data: T) : DataState<T>

    data object Empty : DataState<Nothing>

    data object Loading : DataState<Nothing>
}

fun <T> DataState<T>.valueOrNull(): T? = when (this) {
    is DataState.Success -> data
    else -> null
}

fun <T> DataState<T>.orThrow(): T = when (this) {
    is DataState.Success -> data
    DataState.Empty -> throw NullPointerException()
    is DataState.Error -> throw IllegalStateException(message, throwable)
    DataState.Loading -> throw IllegalStateException()
}

fun <T, R> DataState<T>.map(mapper: (T) -> R): DataState<R> =
    when (this) {
        is DataState.Success -> {
            DataState.Success(mapper(this.data))
        }

        else -> {
            @Suppress("UNCHECKED_CAST")
            this as DataState<R>
        }
    }
