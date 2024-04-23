package org.singing.app.domain.model.audio

import java.io.File

actual data class AudioFile(
    val file: File,
    actual val duration: Long,
)
