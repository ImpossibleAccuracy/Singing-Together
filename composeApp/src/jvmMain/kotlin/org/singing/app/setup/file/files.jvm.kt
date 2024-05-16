package org.singing.app.setup.file

import com.singing.audio.utils.ComposeFile
import java.nio.file.Path
import kotlin.io.path.createParentDirectories


actual fun readFileBytes(file: ComposeFile): ByteArray {
    return file.file.readBytes()
}

actual fun writeFile(filename: Path, data: ByteArray) {
    filename.createParentDirectories()

    val outFile = filename.toFile()

    outFile.createNewFile()

    outFile.writeBytes(data)
}
