package org.singing.app.setup.file

import androidx.compose.runtime.Composable
import com.singing.audio.utils.ComposeFile
import kotlinx.collections.immutable.ImmutableList

@Composable
expect fun FilePicker(
    show: Boolean,
    initialDirectory: String?,
    fileExtensions: ImmutableList<String>,
    title: String?,
    onFileSelected: (ComposeFile?) -> Unit,
)

@Composable
fun FilePicker(
    show: Boolean,
    fileExtensions: ImmutableList<String>,
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
