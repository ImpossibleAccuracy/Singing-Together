package com.singing.audio.player.model

import com.singing.audio.utils.ComposeFile

actual data class AudioFile(
    actual val name: String,
    actual val file: ComposeFile,
    actual val duration: Long,
)
