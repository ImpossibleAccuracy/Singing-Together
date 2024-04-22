package org.singing.app.setup.audio

import com.singing.audio.library.params.DecoderParams

actual object AudioDefaults {
    actual val VoiceBufferSize: Int = 1024 * 64

    actual val VoiceDecoderParams: DecoderParams =
        DecoderParams(
            bufferSize = VoiceBufferSize,
            frameRate = 48000F,
            frameSize = 2,
            sampleRate = 48000F,
            sampleSizeInBits = 16,
            channels = 1,
            isBigEndian = false,
        )
}
