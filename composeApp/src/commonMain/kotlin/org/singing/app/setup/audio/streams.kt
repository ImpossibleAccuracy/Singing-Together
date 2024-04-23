package org.singing.app.setup.audio

import com.singing.audio.library.decoder.frequency.FrequencyDecoder
import com.singing.audio.library.input.AudioInput
import com.singing.audio.library.params.AudioParams

expect fun getSoundInput(data: AudioParams): AudioInput?

fun getSoundInput() = getSoundInput(
    data = AudioDefaults.VoiceInputAudioParams
)

fun createSimpleFrequencyDecoder() =
    FrequencyDecoder(
        bufferSize = AudioDefaults.VoiceInputAudioParams.bufferSize,
        data = AudioDefaults.VoiceInputAudioParams
    )
