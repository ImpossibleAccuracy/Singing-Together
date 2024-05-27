package com.singing.app.base

import androidx.compose.runtime.Stable

@Stable
expect class ComposeFile {
    val fullPath: String
}
