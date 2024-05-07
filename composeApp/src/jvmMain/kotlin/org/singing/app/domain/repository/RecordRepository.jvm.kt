package org.singing.app.domain.repository

import com.singing.audio.player.model.AudioFile
import java.nio.file.Path
import kotlin.io.path.createParentDirectories

actual fun writeFile(filename: Path, data: ByteArray) {
    filename.createParentDirectories()

    val outFile = filename.toFile()

    outFile.createNewFile()

    outFile.writeBytes(data)
}

actual fun readFileBytes(file: AudioFile): ByteArray {
    return file.file.readBytes()
}
