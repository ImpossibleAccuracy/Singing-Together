package com.singing.audio.devices.model

data class AudioDevice(
    val title: String,
    val type: AudioDeviceType,
    val isDefault: Boolean,
    val data: AudioDevicePlatformData
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AudioDevice

        if (title != other.title) return false
        if (type != other.type) return false
        if (isDefault != other.isDefault) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + isDefault.hashCode()
        return result
    }
}
