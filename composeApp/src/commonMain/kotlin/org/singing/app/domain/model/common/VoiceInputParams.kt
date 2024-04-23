package org.singing.app.domain.model.common

import com.singing.audio.library.params.AudioParams

data class VoiceInputParams(
    val bufferSize: Int,
    val audioParams: AudioParams,
)
