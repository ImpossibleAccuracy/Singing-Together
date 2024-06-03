package com.singing.config.track

import com.singing.audio.library.filter.AudioFilter

actual object TrackProperties : ITrackProperties {
    override val bufferSize: Int = DefaultTrackProperties.bufferSize
    override val defaultFilters: List<AudioFilter> = DefaultTrackProperties.defaultFilters
    override val allowedSoundFormats: Map<String, List<String>> = DefaultTrackProperties.allowedSoundFormats
}