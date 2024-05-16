package org.singing.app.domain.repository.track

import com.singing.audio.player.model.AudioFile
import com.singing.audio.utils.ComposeFile
import kotlin.random.Random.Default.nextLong

actual fun createTestFile(): AudioFile {
    val path = generateString(15)

    return AudioFile(
        file = ComposeFile(),
        name = generateString(15),
        duration = nextLong(10000, 120000),
    )
}
