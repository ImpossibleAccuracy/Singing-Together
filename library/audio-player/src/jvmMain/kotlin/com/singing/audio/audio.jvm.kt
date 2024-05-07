package com.singing.audio

import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import java.io.File


actual suspend fun getFileDuration(file: File): Long {
    val media = Media(file.toURI().toString())

    val player = MediaPlayer(media)

    player.waitReady()

    return player.totalDuration.toMillis().toLong()
}


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

