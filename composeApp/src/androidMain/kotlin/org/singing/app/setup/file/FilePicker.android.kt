package org.singing.app.setup.file

import androidx.compose.runtime.Composable
import com.singing.audio.utils.ComposeFile

@Composable
actual fun FilePicker(
    show: Boolean,
    initialDirectory: String?,
    fileExtensions: List<String>,
    title: String?,
    onFileSelected: (ComposeFile?) -> Unit
) {
}
