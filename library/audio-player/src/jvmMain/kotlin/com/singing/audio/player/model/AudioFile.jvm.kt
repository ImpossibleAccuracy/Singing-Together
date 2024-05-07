package com.singing.audio.player.model

import java.io.File

actual data class AudioFile(
    val file: File,
    actual val name: String,
    actual val duration: Long,
) {
    actual val path: String
        get() = file.absolutePath
}
