package com.singing.feature.recording.setup.viewmodel

import com.singing.app.base.ComposeFile

sealed interface SelectAudioIntent {
    data class ProcessAudio(val file: ComposeFile) : SelectAudioIntent

    data object ClearTrackData : SelectAudioIntent
}