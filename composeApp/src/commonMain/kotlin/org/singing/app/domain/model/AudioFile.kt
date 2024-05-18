package org.singing.app.domain.model

import androidx.compose.runtime.Stable
import com.singing.audio.utils.ComposeFile

@Stable
data class AudioFile(
    val file: ComposeFile,
    val name: String,
    val duration: Long,
)
