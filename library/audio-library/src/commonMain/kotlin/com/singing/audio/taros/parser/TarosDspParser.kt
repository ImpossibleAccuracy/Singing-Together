package com.singing.audio.taros.parser

import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor
import com.singing.audio.library.model.AudioParams
import com.singing.audio.library.parser.AudioParser
import com.singing.audio.taros.decoder.TarosDspDecoder
import com.singing.audio.taros.filter.TarosAudioFilter
import com.singing.audio.taros.input.TarosDspInput
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class TarosDspParser<T>(
    private val params: AudioParams,
    private val input: TarosDspInput,
    private val decoder: TarosDspDecoder<T>,
    private val filters: List<TarosAudioFilter> = listOf(),
) : AudioParser<T> {
    override fun release() {
        input.dispatcher.stop()
    }

    override fun parse(): Flow<T> = callbackFlow {
        with(input) {
            filters.forEach { filter ->
                dispatcher.addAudioProcessor(filter.toDispatcher())
            }

            val pitchProcessor = createPitchProcessor {
                trySend(it)
            }

            dispatcher.addAudioProcessor(pitchProcessor)

            decoder.init()

            dispatcher.run()

            close()
            release()

            awaitClose {
                release()
            }
        }
    }

    private fun createPitchProcessor(
        callback: (T) -> Unit
    ): PitchProcessor {
        val handler = PitchDetectionHandler { res, e ->
            val result = decoder.transform(res, e)

            if (result != null) {
                callback(result)
            }
        }

        return PitchProcessor(
            PitchProcessor.PitchEstimationAlgorithm.FFT_YIN,
            params.sampleRate,
            params.bufferSize,
            handler,
        )
    }
}