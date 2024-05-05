package org.singing.app.ui.screens.record.state

import com.singing.audio.player.model.AudioFile
import org.singing.app.ui.screens.record.model.RecordItem

data class AudioProcessState(
    val selectedAudio: AudioFile,
    val isParsing: Boolean = false,
    val data: List<RecordItem> = listOf()
)
