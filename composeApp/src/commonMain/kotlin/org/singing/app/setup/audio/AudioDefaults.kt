package org.singing.app.setup.audio

import com.singing.audio.library.params.AudioParams

expect object AudioDefaults {
    val VoiceInputAudioParams: AudioParams

    val AllowedSoundFormats: List<String>
}
