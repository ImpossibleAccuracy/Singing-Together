package org.singing.app.setup.file

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.singing.audio.utils.ComposeFile
import javafx.application.Platform
import javafx.stage.FileChooser
import java.io.File
import java.util.prefs.Preferences

private val preferences = Preferences.userRoot().node("file-chooser")

private const val KEY = "LastDirectory"

@Composable
actual fun FilePicker(
    show: Boolean,
    initialDirectory: String?,
    fileExtensions: List<String>,
    title: String?,
    onFileSelected: (ComposeFile?) -> Unit
) {
    LaunchedEffect(show) {
        if (!show) return@LaunchedEffect

        Platform.runLater {
            val fileChooser = FileChooser()

            if (title != null) {
                fileChooser.title = title
            }

            val directory = initialDirectory ?: preferences.get(KEY, System.getProperty("user.home"))

            fileChooser.initialDirectory = File(directory)

            fileChooser.extensionFilters.addAll(
                FileChooser.ExtensionFilter(
                    "Audio",
                    fileExtensions.joinToString(";") { "*.$it" },
                )
            )

            val result = fileChooser.showOpenDialog(null)

            if (result == null) {
                onFileSelected(null)
            } else {
                preferences.put(KEY, result.absoluteFile.parent)

                onFileSelected(
                    ComposeFile(result)
                )
            }
        }
    }
}


/*@Composable
actual fun FilePicker(
    show: Boolean,
    initialDirectory: String?,
    fileExtensions: List<String>,
    title: String?,
    onFileSelected: (ComposeFile?) -> Unit
) {
    LaunchedEffect(show) {
        val result = FileDialog(null as Frame?, title, FileDialog.LOAD).apply {
            isMultipleMode = false

            // windows
            file = fileExtensions.joinToString(";") { "*.$it" }

            // linux
            setFilenameFilter { _, name ->
                fileExtensions.any {
                    name.endsWith(it)
                }
            }

            isVisible = show
        }

        if (show && result.isValid) {
            if (result.file == null) {
                onFileSelected(null)
            } else {
                onFileSelected(
                    ComposeFile(
                        File(result.file)
                    )
                )
            }
        }
    }
}*/
