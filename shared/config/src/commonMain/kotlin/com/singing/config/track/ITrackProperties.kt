package com.singing.config.track

import com.singing.audio.library.filter.AudioFilter

interface ITrackProperties {
    val bufferSize: Int
    val defaultFilters: List<AudioFilter>

    val allowedSoundFormats: Map<String, List<String>>
}