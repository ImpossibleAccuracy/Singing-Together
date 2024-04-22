package org.singing.app.setup.audio

import com.singing.audio.library.input.AudioInput
import com.singing.audio.library.input.DataLineAudioInput
import com.singing.audio.library.input.ext.toAudioFormat
import org.singing.app.domain.model.VoiceInputParams
import javax.sound.sampled.AudioSystem

actual fun getVoiceInputStream(
    data: VoiceInputParams,
): AudioInput? {
    val audioFormat = data.decoderParams.toAudioFormat()

    try {
        val dataLine = AudioSystem.getTargetDataLine(audioFormat)

        return DataLineAudioInput(dataLine, AudioDefaults.VoiceDecoderParams)
    } catch (_: Exception) {
        return null
    }
}
