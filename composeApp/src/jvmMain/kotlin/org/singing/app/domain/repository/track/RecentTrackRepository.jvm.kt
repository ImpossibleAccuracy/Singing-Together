package org.singing.app.domain.repository.track

import com.singing.audio.player.model.AudioFile
import com.singing.audio.utils.ComposeFile
import java.io.File
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random.Default.nextLong

val index = AtomicInteger()

actual fun createTestFile(): AudioFile {
    val path = generateString(15)

    return AudioFile(
        file = ComposeFile(
            File(path)
        ),
        name = generateString(15),
        duration = nextLong(10000, 120000),
    )
}
