package org.singing.app.domain.model

import com.singing.audio.library.params.DecoderParams

data class VoiceInputParams(
    val bufferSize: Int,
    val decoderParams: DecoderParams,
)
