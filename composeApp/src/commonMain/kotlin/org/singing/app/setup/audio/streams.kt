package org.singing.app.setup.audio

import com.singing.audio.library.decoder.frequency.FrequencyDecoder
import com.singing.audio.library.input.AudioInput
import org.singing.app.domain.model.VoiceInputParams

expect fun getVoiceInputStream(
    data: VoiceInputParams
): AudioInput?

fun getVoiceInputStream() =
    getVoiceInputStream(
        VoiceInputParams(
            bufferSize = AudioDefaults.VoiceBufferSize,
            decoderParams = AudioDefaults.VoiceDecoderParams
        )
    )

fun createSimpleFrequencyDecoder() =
    FrequencyDecoder(
        bufferSize = AudioDefaults.VoiceBufferSize,
        data = AudioDefaults.VoiceDecoderParams
    )
