package com.singing.app.audio

import com.singing.app.base.ComposeFile
import com.singing.domain.model.AudioFile
import java.io.File


expect suspend fun getFileDuration(file: File): Long

expect suspend fun processAudioFile(inputFile: ComposeFile): AudioFile?
