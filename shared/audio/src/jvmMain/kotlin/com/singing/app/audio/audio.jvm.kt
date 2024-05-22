package com.singing.app.audio

import com.singing.app.domain.model.AudioFile
import com.singing.audio.utils.ComposeFile
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.File


actual suspend fun getFileDuration(file: File): Long {
    val media = Media(file.toURI().toString())
    val player = MediaPlayer(media)

    player.waitReady()

    val duration = player.totalDuration.toMillis().toLong()

    player.dispose()

    return duration
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


actual suspend fun processAudioFile(inputFile: ComposeFile): AudioFile? {
    val file = inputFile.file

    val duration = withContext(Dispatchers.IO) {
        getFileDuration(file)
    }

    return AudioFile(
        file = ComposeFile(file),
        name = file.name,
        duration = duration,
    )
}
