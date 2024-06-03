package com.singing.app.data.setup.file

import com.singing.app.base.ComposeFile
import com.singing.app.data.setup.PlatformInitParams
import java.io.InputStream
import java.nio.file.Path
import kotlin.io.path.createFile
import kotlin.io.path.createParentDirectories
import kotlin.io.path.exists

actual class FileStoreProperties(
    val normalStorePath: Path,
    val tempPath: Path,

    val filenameLength: Int,
)

actual class FileStore actual constructor(
    private val init: PlatformInitParams,
    private val properties: FileStoreProperties,
) {
    companion object {
        @JvmStatic
        fun getRandomString(length: Int): String {
            val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
            return (1..length)
                .map { allowedChars.random() }
                .joinToString("")
        }
    }

    actual fun storeFile(data: InputStream): ComposeFile {
        val name = getRandomString(properties.filenameLength)

        val file = properties.normalStorePath
            .resolve("$name.wav")
            .createParentDirectories()
            .createFile()
            .toFile()

        file.createNewFile()

        file.outputStream().use {
            data.copyTo(it)
        }

        return ComposeFile(file)
    }

    actual fun copyToStore(data: ComposeFile): ComposeFile {
        val name = getRandomString(properties.filenameLength)

        val file = properties.normalStorePath
            .resolve("$name.${data.file.extension}")
            .createParentDirectories()
            .createFile()
            .toFile()

        file.outputStream().use {
            data.inputStream().copyTo(it)
        }

        return ComposeFile(file)
    }

    actual fun getFile(
        recordId: Int,
        type: String
    ): ComposeFile? {
        val tempDirectory = properties.tempPath.toFile()

        return tempDirectory.listFiles()
            ?.firstOrNull {
                it.name.startsWith("${recordId}_$type")
            }?.let {
                ComposeFile(it)
            }
    }

    actual fun createRecordTempFile(
        recordId: Int,
        type: String,
        extension: String,
        data: InputStream
    ): ComposeFile {
        val filePath = properties.tempPath.resolve("${recordId}_$type.$extension")

        if (filePath.exists())
            return ComposeFile(filePath.toFile())

        filePath.createParentDirectories()

        val file = filePath.createFile().toFile()

        file.outputStream().use { fileOut ->
            data.copyTo(fileOut)
        }

        return ComposeFile(file)
    }
}
