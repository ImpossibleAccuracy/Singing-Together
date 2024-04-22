package org.singing.app.ui.screens.record

import cafe.adriel.voyager.core.model.ScreenModel
import com.singing.audio.devices.AudioDevices
import com.singing.audio.devices.model.AudioDevice
import com.singing.audio.devices.model.AudioDeviceType
import com.singing.audio.library.input.DataLineAudioInput
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.singing.app.setup.audio.AudioDefaults
import org.singing.app.setup.audio.VoiceInput
import javax.sound.sampled.TargetDataLine


@OptIn(DelicateCoroutinesApi::class)
class RecordScreenAudioViewModel : ScreenModel {
    val microphones = AudioDevices.Devices
        .map { list ->
            list.filter {
                it.type == AudioDeviceType.Input
            }
        }
        .stateIn(GlobalScope, SharingStarted.Lazily, listOf())


    val speakers = AudioDevices.Devices
        .map { list ->
            list.filter {
                it.type == AudioDeviceType.Output
            }
        }
        .stateIn(GlobalScope, SharingStarted.Lazily, listOf())

    val selectedVoiceInput = MutableStateFlow<AudioDevice?>(null)

    val selectedVoiceOutput = MutableStateFlow<AudioDevice?>(null)

    val selectedAudioOutput = MutableStateFlow<AudioDevice?>(null)

    init {
        GlobalScope.launch {
            launch {
                listenForInputsUpdate()
            }

            launch {
                listenForOutputsUpdate()
            }

            launch {
                listenForSelectedInputUpdate()
            }
        }
    }

    private suspend fun listenForInputsUpdate() {
        microphones
            .filter {
                it.isNotEmpty()
            }
            .collect { devices ->
                if (selectedVoiceInput.value == null ||
                    selectedVoiceInput.value !in devices
                ) {
                    selectedVoiceInput.value = devices.firstOrNull()
                }
            }
    }

    private suspend fun listenForOutputsUpdate() {
        speakers
            .filter {
                it.isNotEmpty()
            }
            .collect { devices ->
                if (!devices.contains(selectedVoiceOutput.value)) {
                    selectedVoiceOutput.value = null
                }

                if (selectedAudioOutput.value == null ||
                    !devices.contains(selectedAudioOutput.value)
                ) {
                    selectedAudioOutput.value = devices.firstOrNull()
                }
            }
    }

    private suspend fun listenForSelectedInputUpdate() {
        selectedVoiceInput
            .collect { device ->
                val dataLine = device?.data?.line as? TargetDataLine

                val input = dataLine?.let { DataLineAudioInput(it, AudioDefaults.VoiceDecoderParams) }

                VoiceInput.setAudioInput(input)
            }
    }
}
