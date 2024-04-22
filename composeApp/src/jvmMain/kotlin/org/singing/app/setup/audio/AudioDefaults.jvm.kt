package org.singing.app.setup.audio

import com.singing.audio.devices.DevicesScanner
import com.singing.audio.library.input.ext.toAudioFormat
import com.singing.audio.library.params.DecoderParams

actual object AudioDefaults {
    actual val VoiceBufferSize: Int = 1024 * 64

    actual val VoiceDecoderParams: DecoderParams =
        DecoderParams(
            bufferSize = VoiceBufferSize,
            frameRate = 41400F,
            frameSize = 2,
            sampleRate = 41400F,
            sampleSizeInBits = 16,
            channels = 1,
            isBigEndian = false,
        )

    init {
        DevicesScanner.inputAudioFormat = VoiceDecoderParams.toAudioFormat()
    }
}
