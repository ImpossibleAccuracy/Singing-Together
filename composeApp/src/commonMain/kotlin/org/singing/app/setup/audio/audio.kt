package org.singing.app.setup.audio

import org.singing.app.domain.model.AudioFile
import com.singing.audio.utils.ComposeFile

expect suspend fun processAudioFile(inputFile: ComposeFile): AudioFile?
