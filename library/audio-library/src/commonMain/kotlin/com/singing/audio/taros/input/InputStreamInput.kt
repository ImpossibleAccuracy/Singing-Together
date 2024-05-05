package com.singing.audio.taros.input

import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.io.TarsosDSPAudioFormat
import be.tarsos.dsp.io.UniversalAudioInputStream
import com.singing.audio.audio.convertChannels
import com.singing.audio.audio.openAudioStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import javax.sound.sampled.AudioFormat

suspend fun openInputStreamAsTarosDspInput(inputStream: InputStream, bufferSize: Int): TarosDspInput {
    val audioStream = withContext(Dispatchers.IO) {
        openAudioStream(inputStream)
    }

    val monoAudioStream = convertChannels(audioStream, 1)

    val dispatcher = createAudioDispatcher(monoAudioStream, monoAudioStream.format, bufferSize)

    return TarosDspInput(dispatcher)
}

private fun createAudioDispatcher(file: InputStream, format: AudioFormat, bufferSize: Int) =
    AudioDispatcher(
        UniversalAudioInputStream(
            file,
            TarsosDSPAudioFormat(
                TarsosDSPAudioFormat.Encoding.PCM_SIGNED,
                format.sampleRate,
                format.sampleSizeInBits,
                format.channels,
                format.frameSize,
                format.frameRate,
                format.isBigEndian,
            )
        ),
        bufferSize,
        0,
    )
