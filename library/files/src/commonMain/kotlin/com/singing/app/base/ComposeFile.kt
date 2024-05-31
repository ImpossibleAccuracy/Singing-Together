package com.singing.app.base

import androidx.compose.runtime.Stable

@Stable
expect class ComposeFile {
    val name: String

    val fullPath: String

    fun readAll(): ByteArray
}

expect fun openFile(path: String): ComposeFile

expect fun exists(file: ComposeFile): Boolean

expect fun exists(path: String): Boolean

val ComposeFile.exists: Boolean
    get() = exists(this)
