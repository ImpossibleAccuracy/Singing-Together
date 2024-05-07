package org.singing.app.setup.audio

import com.singing.audio.player.model.AudioFile
import org.singing.app.setup.file.ComposeFile

expect suspend fun processAudioFile(inputFile: ComposeFile): AudioFile?
