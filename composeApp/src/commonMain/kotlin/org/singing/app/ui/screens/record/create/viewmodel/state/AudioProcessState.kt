package org.singing.app.ui.screens.record.create.viewmodel.state

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import com.singing.app.domain.model.AudioFile
import org.singing.app.ui.screens.record.create.viewmodel.model.RecordItem

data class AudioProcessState(
    val selectedAudio: AudioFile,
    val isParsing: Boolean = false,
    val data: ImmutableList<RecordItem> = persistentListOf()
)
