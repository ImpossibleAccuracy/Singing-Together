package org.singing.app.domain.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

abstract class StateRepository<T>(
    defaultValue: List<T> = listOf()
) {
    private val _items = MutableStateFlow<List<T>>(defaultValue)
    protected val items = _items

    protected fun addSingle(item: T) {
        _items.update {
            it.plus(item)
        }
    }

    protected fun update(function: (List<T>) -> List<T>) {
        _items.update(function)
    }

    protected fun updateSingle(item: T, function: (T) -> T?): T? =
        updateSingle(
            predicate = {
                it == item
            },
            function = function,
        )

    protected fun updateSingle(predicate: (T) -> Boolean, function: (T) -> T?): T? {
        val data = _items.value.toMutableList()

        val index = data.indexOfFirst(predicate)

        if (index == -1) {
            throw NoSuchElementException()
        }

        val newItem = function(data[index])

        when (newItem) {
            null -> data.removeAt(index)
            else -> data[index] = newItem
        }

        _items.value = data

        return newItem
    }
}
