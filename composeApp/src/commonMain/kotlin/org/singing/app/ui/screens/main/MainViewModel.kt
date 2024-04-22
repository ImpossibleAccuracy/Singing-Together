package org.singing.app.ui.screens.main

import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel : ScreenModel {
    val isAnimate = MutableStateFlow(false)
}
