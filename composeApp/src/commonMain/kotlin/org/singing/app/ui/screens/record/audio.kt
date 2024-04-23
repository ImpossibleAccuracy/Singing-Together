package org.singing.app.ui.screens.record

import com.darkrockstudios.libraries.mpfilepicker.MPFile
import org.singing.app.domain.model.audio.AudioFile

expect suspend fun processAudioFile(inputFile: MPFile<Any>): AudioFile?
