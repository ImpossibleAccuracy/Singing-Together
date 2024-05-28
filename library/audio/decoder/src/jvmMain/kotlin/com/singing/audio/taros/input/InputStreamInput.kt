package com.singing.audio.taros.input

import com.singing.audio.audio.convertChannels
import com.singing.audio.audio.openAudioStream
import com.singing.audio.taros.createAudioDispatcher
import java.io.InputStream

fun openInputStreamAsTarosDspInput(inputStream: InputStream, bufferSize: Int): TarosDspInput {
    val audioStream = openAudioStream(inputStream)

    val monoAudioStream = convertChannels(audioStream, 1)

    val dispatcher = createAudioDispatcher(monoAudioStream, monoAudioStream.format, bufferSize)

    return TarosDspInput(dispatcher)
}
