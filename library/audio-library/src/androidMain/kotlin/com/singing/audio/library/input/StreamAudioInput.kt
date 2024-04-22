package com.singing.audio.library.input

import android.media.AudioRecord

class StreamAudioInput(
    private val track: AudioRecord
) : AudioInput {
    override fun readBytes(buf: ByteArray): Boolean {
        return track.read(buf, 0, buf.size) > 0
    }
}