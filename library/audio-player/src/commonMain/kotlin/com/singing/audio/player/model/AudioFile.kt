package com.singing.audio.player.model

import com.singing.audio.utils.ComposeFile

expect class AudioFile {
    val name: String
    val file: ComposeFile
    val duration: Long
}
