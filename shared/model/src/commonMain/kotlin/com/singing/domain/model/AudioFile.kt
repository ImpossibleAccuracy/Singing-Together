package com.singing.domain.model

import androidx.compose.runtime.Stable
import com.singing.app.base.ComposeFile

@Stable
data class AudioFile(
    val file: ComposeFile,
    val name: String,
    val duration: Long,
)
