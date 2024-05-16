package org.singing.app.setup.audio

import com.singing.audio.player.model.AudioFile
import com.singing.audio.utils.ComposeFile

expect suspend fun processAudioFile(inputFile: ComposeFile): AudioFile?
