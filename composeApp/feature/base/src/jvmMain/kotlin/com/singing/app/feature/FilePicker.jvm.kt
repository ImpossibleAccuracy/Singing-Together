package com.singing.app.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.singing.app.base.ComposeFile
import javafx.application.Platform
import javafx.stage.FileChooser
import kotlinx.collections.immutable.PersistentList
import java.io.File
import java.util.prefs.Preferences


private val preferences = Preferences.userRoot().node("file-chooser")

private const val KEY = "LastDirectory"

@Composable
actual fun FilePicker(
    show: Boolean,
    initialDirectory: String?,
    fileExtensions: PersistentList<String>,
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