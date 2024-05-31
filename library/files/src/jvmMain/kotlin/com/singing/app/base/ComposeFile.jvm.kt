package com.singing.app.base

import androidx.compose.runtime.Stable
import java.io.File
import java.io.InputStream
import java.net.URI

@Stable
actual data class ComposeFile(
    val file: File,
) {
    actual val name: String
        get() = file.nameWithoutExtension

    actual val fullPath: String
        get() = file.absolutePath

    fun inputStream(): InputStream =
        file.inputStream()

    val uri: URI
        get() = file.toURI()

    actual fun readAll(): ByteArray {
        return file.readBytes()
    }
}

actual fun openFile(path: String): ComposeFile {
    return ComposeFile(
        File(path)
    )
}

actual fun exists(file: ComposeFile): Boolean {
    return file.file.exists()
}

actual fun exists(path: String): Boolean {
    return File(path).exists()
}
