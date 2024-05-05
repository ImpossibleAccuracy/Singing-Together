package org.singing.app.setup.audio

import com.darkrockstudios.libraries.mpfilepicker.MPFile
import com.singing.audio.player.model.AudioFile

expect suspend fun processAudioFile(inputFile: MPFile<Any>): AudioFile?
