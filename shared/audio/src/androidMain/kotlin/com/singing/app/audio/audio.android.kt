package com.singing.app.audio

import com.singing.app.domain.model.AudioFile
import com.singing.audio.utils.ComposeFile
import java.io.File


actual suspend fun getFileDuration(file: File): Long {
    TODO("Not yet implemented")
}

actual suspend fun processAudioFile(inputFile: ComposeFile): AudioFile? {
    TODO("Not yet implemented")
}
