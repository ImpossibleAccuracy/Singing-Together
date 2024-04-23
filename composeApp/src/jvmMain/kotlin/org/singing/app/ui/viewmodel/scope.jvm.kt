package org.singing.app.ui.viewmodel

import cafe.adriel.voyager.core.model.ScreenModel
import com.singing.audio.utils.backgroundScope
import kotlinx.coroutines.CoroutineScope

actual val ScreenModel.viewModelScope: CoroutineScope
    get() = backgroundScope
