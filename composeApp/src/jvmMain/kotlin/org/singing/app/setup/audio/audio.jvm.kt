package org.singing.app.setup.audio

import com.singing.audio.getFileDuration
import com.singing.audio.player.model.AudioFile
import com.singing.audio.utils.ComposeFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

