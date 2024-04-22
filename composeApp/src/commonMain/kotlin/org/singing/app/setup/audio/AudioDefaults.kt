package org.singing.app.setup.audio

import com.singing.audio.library.params.DecoderParams

expect object AudioDefaults {
    val VoiceBufferSize: Int
    val VoiceDecoderParams: DecoderParams
}
