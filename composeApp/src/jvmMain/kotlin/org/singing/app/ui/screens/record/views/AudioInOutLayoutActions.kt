package org.singing.app.ui.screens.record.views

import androidx.compose.runtime.Immutable
import com.singing.audio.devices.model.AudioDevice

@Immutable
data class AudioInOutLayoutActions(
    val onVoiceInputSelected: (AudioDevice) -> Unit,
    val onVoiceOutputSelected: (AudioDevice?) -> Unit,
    val onAudioOutputSelected: (AudioDevice?) -> Unit,
)
