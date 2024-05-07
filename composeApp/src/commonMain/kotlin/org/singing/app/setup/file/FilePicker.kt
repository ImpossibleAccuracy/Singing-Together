package org.singing.app.setup.file

import androidx.compose.runtime.Composable

@Composable
expect fun FilePicker(
    show: Boolean,
    initialDirectory: String?,
    fileExtensions: List<String>,
    title: String?,
    onFileSelected: (ComposeFile?) -> Unit,
)

@Composable
fun FilePicker(
    show: Boolean,
    fileExtensions: List<String>,
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
