package com.singing.audio.library.looper

import com.singing.audio.library.params.AudioParams

class TotalTimeDecoderLoop(
    private val data: AudioParams,
    private val totalTime: Long,
) : DecoderLoop {

    private var playedTime: Long = 0

    override fun onStart() {
        playedTime = 0
    }

    override fun isResume(samples: FloatArray): Boolean {
        val playedTimeSeconds = samples.size / data.frameRate
        playedTime += (playedTimeSeconds * 1000).toLong()

        return playedTime <= totalTime
    }
}
