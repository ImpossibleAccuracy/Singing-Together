package com.singing.audio.taros.filter

import be.tarsos.dsp.AudioProcessor
import com.singing.audio.library.filter.AudioFilter

interface TarosAudioFilter : AudioFilter {
    fun toDispatcher(): AudioProcessor
}
