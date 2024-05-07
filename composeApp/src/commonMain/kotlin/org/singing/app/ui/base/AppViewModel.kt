package org.singing.app.ui.base

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

abstract class AppViewModel : ScreenModel {
    val viewModelScope: CoroutineScope by lazy { CoroutineScope(Dispatchers.Main) }

    override fun onDispose() {
        super.onDispose()

        viewModelScope.cancel()
    }
}
