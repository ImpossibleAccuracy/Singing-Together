package org.singing.app.setup.audio

import com.singing.audio.getFileDuration
import com.singing.audio.player.model.AudioFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.singing.app.setup.file.ComposeFile

actual suspend fun processAudioFile(inputFile: ComposeFile): AudioFile? {
    val file = inputFile.file

    val duration = withContext(Dispatchers.IO) {
        getFileDuration(file)
    }

    return AudioFile(
        file = file,
        name = file.name,
        duration = duration,
    )
}

