package com.singing.app.base

import androidx.compose.runtime.Stable

@Stable
actual class ComposeFile {
    actual val name: String
        get() = TODO("Not yet implemented")

    actual val fullPath: String
        get() = TODO("Not yet implemented")

    actual fun readAll(): ByteArray {
        TODO("Not yet implemented")
    }
}

actual fun openFile(path: String): ComposeFile {
    TODO("Not yet implemented")
}