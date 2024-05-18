package com.singing.audio.utils

import androidx.compose.runtime.Stable
import java.io.File
import java.io.InputStream
import java.net.URI

@Stable
actual data class ComposeFile(
    val file: File,
) {
    actual val fullPath: String
        get() = file.absolutePath

    fun inputStream(): InputStream =
        file.inputStream()

    val uri: URI
        get() = file.toURI()
}
