package org.singing.app.setup.audio

import javax.sound.sampled.Mixer
import javax.sound.sampled.Port

val Mixer.isAvailable: Boolean
    get() =
        targetLineInfo.isNotEmpty()

val Mixer.isInput: Boolean
    get() =
        isLineSupported(Port.Info.LINE_IN) ||
                isLineSupported(Port.Info.MICROPHONE)

val Mixer.isOutput: Boolean
    get() =
        isLineSupported(Port.Info.LINE_OUT) ||
                isLineSupported(Port.Info.SPEAKER) ||
                isLineSupported(Port.Info.HEADPHONE)
