package com.singing.audio.player.model

expect class AudioFile {
    val name: String
    val path: String
    val duration: Long
}
