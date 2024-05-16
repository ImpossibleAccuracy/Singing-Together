package org.singing.app.ui.screens.record.create.viewmodel.state

import com.singing.audio.player.model.AudioFile
import org.singing.app.ui.screens.record.create.viewmodel.model.RecordItem

data class AudioProcessState(
    val selectedAudio: AudioFile,
    val isParsing: Boolean = false,
    val data: List<RecordItem> = listOf()
)
