package org.singing.app.ui.screens.record

import com.darkrockstudios.libraries.mpfilepicker.MPFile
import javafx.scene.media.Media
import javafx.scene.media.MediaPlayer
import org.singing.app.domain.audio.player.waitReady
import org.singing.app.domain.model.audio.AudioFile
import java.io.File

actual suspend fun processAudioFile(inputFile: MPFile<Any>): AudioFile? {
    val file = inputFile.platformFile as File

    val media = Media(file.toURI().toString())

    val player = MediaPlayer(media)

    player.waitReady()

    return AudioFile(
        file = file,
        duration = player.totalDuration.toMillis().toLong(),
    )
}
