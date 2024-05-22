package com.singing.config.track

import com.singing.audio.library.filter.AudioFilter

actual object TrackProperties : ITrackProperties {
    override val bufferSize: Int = DefaultTrackProperties.bufferSize
    override val defaultFilters: List<AudioFilter> = DefaultTrackProperties.defaultFilters
    override val allowedSoundFormats: List<String> = DefaultTrackProperties.allowedSoundFormats


    val allowedSoundFormatsMimeType: List<String> = listOf(
        "audio/mp4",
        "audio/mpeg",
        "audio/wav",
        "audio/wave",
        "audio/x-wave",
        "audio/x-ms-wma",
        "audio/aac",
    )
}