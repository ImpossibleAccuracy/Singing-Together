package com.singing.app.audio

import com.singing.app.base.ComposeFile
import com.singing.domain.model.AudioFile
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext


actual suspend fun getFileDuration(composeFile: ComposeFile): Long {
    val media = Media(composeFile.file.toURI().toString())
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
        getFileDuration(inputFile)
    }

    return AudioFile(
        file = ComposeFile(file),
        name = file.name,
        duration = duration,
    )
}
