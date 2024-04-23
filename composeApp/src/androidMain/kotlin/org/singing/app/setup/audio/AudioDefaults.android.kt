package org.singing.app.setup.audio

import com.singing.audio.library.params.AudioParams

actual object AudioDefaults {
    actual val VoiceInputAudioParams: AudioParams =
        AudioParams(
            bufferSize = 1024 * 64,
            frameRate = 48000F,
            frameSize = 2,
            sampleRate = 48000F,
            sampleSizeInBits = 16,
            channels = 1,
            isBigEndian = false,
        )

    actual val AllowedSoundFormats: List<String> = listOf(
        "m4a",
        "mp3",
        "wav",
        "wma",
        "aac",
    )
}
