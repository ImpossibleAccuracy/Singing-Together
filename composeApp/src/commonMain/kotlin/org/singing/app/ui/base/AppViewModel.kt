package org.singing.app.ui.base

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.*

abstract class AppViewModel : ScreenModel {
    protected val viewModelScope: CoroutineScope by lazy { CoroutineScope(Dispatchers.Main) }

    fun <T> Flow<List<T>>.stateIn(): StateFlow<ImmutableList<T>> = this
        .map {
            it.toImmutableList()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            persistentListOf()
        )

    override fun onDispose() {
        super.onDispose()

        viewModelScope.cancel()
    }
}
