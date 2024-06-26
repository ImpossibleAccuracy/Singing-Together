package com.singing.audio.library.input

import android.media.AudioRecord
import com.singing.audio.sampled.input.BaseAudioInput

class StreamAudioInput(
    private val track: AudioRecord
) : BaseAudioInput {
    override fun readBytes(buf: ByteArray): Boolean {
        return track.read(buf, 0, buf.size) > 0
    }
}