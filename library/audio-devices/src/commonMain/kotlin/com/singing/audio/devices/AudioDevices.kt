package com.singing.audio.devices

import com.singing.audio.devices.model.AudioDevice
import com.singing.audio.utils.backgroundScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

object AudioDevices {
    private const val UPDATE_RATE = 3000L

    private val scanner by lazy {
        DevicesScanner()
    }

    private val _devices = MutableSharedFlow<List<AudioDevice>>()
    val Devices = _devices.asSharedFlow()

    init {
        backgroundScope.launch {
            while (isActive) {
                updateData()

                delay(UPDATE_RATE)
            }
        }
    }

    private suspend fun updateData() {
        val devices = scanner.getAudioDevices()

        _devices.emit(devices)
    }
}
