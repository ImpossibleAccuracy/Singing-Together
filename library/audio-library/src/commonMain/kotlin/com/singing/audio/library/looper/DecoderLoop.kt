package com.singing.audio.library.looper

fun interface DecoderLoop {
    fun onStart() {}

    fun isResume(samples: FloatArray): Boolean
}
