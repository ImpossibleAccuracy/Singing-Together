package com.singing.audio.devices

import android.content.Context
import android.media.AudioManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.singing.audio.devices.model.AudioDevice
import com.singing.audio.devices.model.AudioDevicePlatformData
import com.singing.audio.devices.model.AudioDeviceType

actual class DevicesScanner {
    // TODO: ?
    @Composable
    actual fun getAudioDevices(): List<AudioDevice> {
        val manager = LocalContext.current.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        val inputDevices = manager.getDevices(AudioManager.GET_DEVICES_INPUTS)
        val outputDevices = manager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)

        val inputs = inputDevices
            .map {
                AudioDevice(
                    title = it.productName.toString().trim(),
                    type = AudioDeviceType.Input,
                    isDefault = it == inputDevices.first(),
                    data = AudioDevicePlatformData(
                        deviceInfo = it
                    )
                )
            }

        val outputs = inputDevices
            .map {
                AudioDevice(
                    title = it.productName.toString().trim(),
                    type = AudioDeviceType.Output,
                    isDefault = it == outputDevices.first(),
                    data = AudioDevicePlatformData(
                        deviceInfo = it
                    )
                )
            }

        return inputs.plus(outputs)
    }
}
