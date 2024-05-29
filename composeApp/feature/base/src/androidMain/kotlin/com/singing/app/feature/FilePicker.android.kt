package com.singing.app.feature

import androidx.compose.runtime.Composable
import com.singing.app.base.ComposeFile
import kotlinx.collections.immutable.PersistentList

@Composable
actual fun FilePicker(
    show: Boolean,
    initialDirectory: String?,
    fileExtensions: PersistentList<String>,
    title: String?,
    onFileSelected: (ComposeFile?) -> Unit
) {
    TODO()
}