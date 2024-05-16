package com.singing.audio.player.model

import com.singing.audio.utils.ComposeFile

actual data class AudioFile(
    actual val file: ComposeFile,
    actual val name: String,
    actual val duration: Long,
)
