package com.singing.audio.capture

actual class AudioCapture {
    /**
     * Starting capture in background
     */
    actual suspend fun capture() {
    }

    /**
     * Wait for capture start
     */
    actual suspend fun awaitStart() {
    }

    /**
     * Stop capture and get result
     */
    actual suspend fun stop(): ByteArray {
        TODO("Not yet implemented")
    }

}