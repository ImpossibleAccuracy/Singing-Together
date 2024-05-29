package com.singing.app.feature

import androidx.compose.runtime.Composable
import com.singing.app.base.ComposeFile
import kotlinx.collections.immutable.PersistentList

@Composable
expect fun FilePicker(
    show: Boolean,
    initialDirectory: String?,
    fileExtensions: PersistentList<String>,
    title: String?,
    onFileSelected: (ComposeFile?) -> Unit,
)

@Composable
fun FilePicker(
    show: Boolean,
    fileExtensions: PersistentList<String>,
    onFileSelected: (ComposeFile?) -> Unit,
) {
    FilePicker(
        show = show,
        title = null,
        initialDirectory = null,
        fileExtensions = fileExtensions,
        onFileSelected = onFileSelected,
    )
}
