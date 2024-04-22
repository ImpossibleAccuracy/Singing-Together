package org.singing.app.domain.model

import com.mohamedrejeb.calf.io.KmpFile

data class AudioFile(
    val file: KmpFile,
    val duration: Long = 1000 * 60 * 3L,
)
