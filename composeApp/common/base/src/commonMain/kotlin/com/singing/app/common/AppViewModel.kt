package com.singing.app.common

import androidx.compose.runtime.Stable
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.*

@Stable
abstract class AppViewModel : ScreenModel {
    fun <T> Flow<T>.stateIn(initial: T): StateFlow<T> = this
        .stateIn(
            screenModelScope,
            SharingStarted.WhileSubscribed(5000),
            initial,
        )

    fun <T> Flow<List<T>>.stateIn(): StateFlow<ImmutableList<T>> = this
        .map { it.toImmutableList() }
        .stateIn(persistentListOf())
}
