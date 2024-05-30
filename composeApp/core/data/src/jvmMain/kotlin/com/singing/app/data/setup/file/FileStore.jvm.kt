package com.singing.app.data.setup.file

import com.singing.app.base.ComposeFile
import com.singing.app.data.setup.PlatformInitParams
import java.io.InputStream
import java.nio.file.Path
import kotlin.io.path.createFile
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

        val file = properties.normalStorePath.resolve(name).toFile()

        file.outputStream().use {
            data.copyTo(it)
        }

        return ComposeFile(file)
    }

    actual fun copyToStore(data: ComposeFile): ComposeFile {
        val name = getRandomString(properties.filenameLength)

        val file = properties.normalStorePath.resolve(name).toFile()

        file.outputStream().use {
            data.inputStream().copyTo(it)
        }

        return ComposeFile(file)
    }

    actual fun createRecordTempFile(
        recordId: Int,
        type: String,
        data: InputStream
    ): ComposeFile {
        val filePath = properties.tempPath.resolve("${recordId}_$type")

        if (filePath.exists())
            return ComposeFile(filePath.toFile())

        val file = filePath.createFile().toFile()

        file.outputStream().use { fileOut ->
            data.copyTo(fileOut)
        }

        return ComposeFile(file)
    }
}
