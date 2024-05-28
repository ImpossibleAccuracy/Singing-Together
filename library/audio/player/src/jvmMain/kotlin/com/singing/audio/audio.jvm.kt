package com.singing.audio

import javafx.scene.media.MediaPlayer
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first


suspend fun MediaPlayer.waitReady() = callbackFlow {
    send(status == MediaPlayer.Status.READY)

    setOnReady {
        trySend(true)
    }

    setOnError {
        println("Error while MediaPlayer prepare: $error")

        cancel()
    }

    awaitClose {
        onReady = null
    }
}.first { it }

