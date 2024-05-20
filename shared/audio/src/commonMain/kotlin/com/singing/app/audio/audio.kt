package com.singing.app.audio

import com.singing.app.domain.model.AudioFile
import com.singing.audio.utils.ComposeFile
import java.io.File


expect suspend fun getFileDuration(file: File): Long

expect suspend fun processAudioFile(inputFile: ComposeFile): AudioFile?
