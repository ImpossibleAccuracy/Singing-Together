package org.singing.app.ui.screens.record.views

import androidx.compose.runtime.Immutable
import com.singing.audio.devices.model.AudioDevice
import org.singing.app.domain.model.ListWithSelected

@Immutable
data class AudioInOutLayoutData(
    val defaultNoChoiceLabel: String,
    val showAudioOutput: Boolean,
    val voiceInputs: ListWithSelected<AudioDevice>,
    val voiceOutputs: ListWithSelected<AudioDevice>?,
    val audioOutputs: ListWithSelected<AudioDevice>?,
)
