package com.singing.audio.taros.parser

import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor
import com.singing.audio.library.model.AudioParams
import com.singing.audio.library.parser.AudioParser
import com.singing.audio.taros.decoder.DecoderResult
import com.singing.audio.taros.decoder.TarosDspDecoder
import com.singing.audio.taros.filter.TarosAudioFilter
import com.singing.audio.taros.input.TarosDspInput
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.yield

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
        filters.forEach { filter ->
            input.dispatcher.addAudioProcessor(filter.toDispatcher())
        }

        val pitchProcessor = createPitchProcessor {
            trySend(it)
        }

        input.dispatcher.addAudioProcessor(pitchProcessor)

        decoder.init()

        input.dispatcher.run()

        close()
        release()

        awaitClose {
            release()
        }
    }

    private fun createPitchProcessor(
        callback: (T) -> Unit
    ): PitchProcessor {
        val handler = PitchDetectionHandler { res, e ->
            val result = decoder.transform(res, e)

            if (result is DecoderResult.Data) {
                callback(result.data)
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