package com.singing.audio.capture

expect class AudioCapture() {
    /**
     * Starting capture in background
     */
    suspend fun capture()

    /**
     * Wait for capture start
     */
    suspend fun awaitStart()

    /**
     * Stop capture and get result
     */
    suspend fun stop(): ByteArray
}
