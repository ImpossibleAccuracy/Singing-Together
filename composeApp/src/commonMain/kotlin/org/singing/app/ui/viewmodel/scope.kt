package org.singing.app.ui.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

interface AppViewModel : ScreenModel {
    override fun onDispose() {
        super.onDispose()

        viewModelScope.cancel()
    }
}

expect val ScreenModel.viewModelScope: CoroutineScope
