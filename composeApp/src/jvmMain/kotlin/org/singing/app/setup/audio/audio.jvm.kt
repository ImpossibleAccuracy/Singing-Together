package org.singing.app.setup.audio

import com.darkrockstudios.libraries.mpfilepicker.MPFile
import com.singing.audio.getFileDuration
import com.singing.audio.player.model.AudioFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

actual suspend fun processAudioFile(inputFile: MPFile<Any>): AudioFile? {
    val file = inputFile.platformFile as File

    val duration = withContext(Dispatchers.IO) {
        getFileDuration(file)
    }

    return AudioFile(
        file = file,
        name = file.name,
        duration = duration,
    )
}

