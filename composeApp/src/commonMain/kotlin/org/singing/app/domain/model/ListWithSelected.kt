package org.singing.app.domain.model

data class ListWithSelected<T>(
    val list: List<T>,
    val selectedItem: T?,
)
