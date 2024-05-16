package org.singing.app.setup.file

import com.singing.audio.utils.ComposeFile
import java.nio.file.Path

expect fun readFileBytes(file: ComposeFile): ByteArray

expect fun writeFile(filename: Path, data: ByteArray)
