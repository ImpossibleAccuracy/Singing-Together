package org.singing.app.setup.audio

import com.singing.audio.library.input.AudioInput
import com.singing.audio.library.input.DataLineAudioInput
import com.singing.audio.library.input.ext.toAudioFormat
import com.singing.audio.library.params.AudioParams
import javax.sound.sampled.AudioSystem

actual fun getSoundInput(
    data: AudioParams,
): AudioInput? =
    AudioLineStore
        .openInput(data.toAudioFormat())
        ?.let {
            DataLineAudioInput(it, data)
        }
