package com.singing.app.base

import androidx.compose.runtime.Stable

@Stable
expect class ComposeFile {
    val name: String

    val fullPath: String

    fun readAll(): ByteArray
}

expect fun openFile(path: String): ComposeFile
