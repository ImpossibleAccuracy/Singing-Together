package com.singing.audio.library.input

import android.media.AudioRecord
import com.singing.audio.sampled.input.BaseAudioInput

class MicrophoneAudioInput(
    private val recorder: AudioRecord
) : BaseAudioInput {
    override fun readBytes(buf: ByteArray): Boolean {
        return recorder.read(buf, 0, buf.size) > 0
    }
}