package org.singing.app.domain.model.audio

import java.io.File

actual data class AudioFile(
    val file: File,
    val duration: Long = 1000 * 60 * 3L,
)
