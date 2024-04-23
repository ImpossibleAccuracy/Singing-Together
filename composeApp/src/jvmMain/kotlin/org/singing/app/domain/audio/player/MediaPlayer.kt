package org.singing.app.domain.audio.player

import javafx.scene.media.MediaPlayer
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first

suspend fun MediaPlayer.waitReady() = callbackFlow {
    send(false)

    setOnReady {
        trySend(true)
    }

    awaitClose {
        onReady = null
    }
}.first { it }
