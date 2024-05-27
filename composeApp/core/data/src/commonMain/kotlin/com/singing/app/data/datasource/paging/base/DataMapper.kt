package com.singing.app.data.datasource.paging.base

fun interface DataMapper<T, R> {
    fun map(source: T): R
}