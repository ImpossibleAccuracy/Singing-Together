package com.singing.audio.library.input

import android.media.AudioRecord

class MicrophoneAudioInput(
    private val recorder: AudioRecord
) : AudioInput {
    override fun readBytes(buf: ByteArray): Boolean {
        return recorder.read(buf, 0, buf.size) > 0
    }
}