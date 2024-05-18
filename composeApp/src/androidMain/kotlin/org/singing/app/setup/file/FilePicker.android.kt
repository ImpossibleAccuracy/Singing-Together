package org.singing.app.setup.file

import androidx.compose.runtime.Composable
import com.singing.audio.utils.ComposeFile
import kotlinx.collections.immutable.ImmutableList

@Composable
actual fun FilePicker(
    show: Boolean,
    initialDirectory: String?,
    fileExtensions: ImmutableList<String>,
    title: String?,
    onFileSelected: (ComposeFile?) -> Unit
) {
}
