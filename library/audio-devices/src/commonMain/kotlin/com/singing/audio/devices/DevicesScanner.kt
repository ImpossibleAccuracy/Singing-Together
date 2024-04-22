package com.singing.audio.devices

import com.singing.audio.devices.model.AudioDevice

expect class DevicesScanner() {
    fun getAudioDevices(): List<AudioDevice>
}
