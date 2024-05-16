package org.singing.app.ui.base

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

abstract class AppViewModel : ScreenModel {
    val viewModelScope: CoroutineScope by lazy { CoroutineScope(Dispatchers.Main) }

    fun <T> Flow<List<T>>.stateIn() =
        stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            listOf()
        )

    override fun onDispose() {
        super.onDispose()

        viewModelScope.cancel()
    }
}
