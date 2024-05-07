package com.singing.audio.taros.input

import com.singing.audio.audio.convertChannels
import com.singing.audio.audio.openAudioStream
import com.singing.audio.taros.createAudioDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream

suspend fun openInputStreamAsTarosDspInput(inputStream: InputStream, bufferSize: Int): TarosDspInput {
    val audioStream = withContext(Dispatchers.IO) {
        openAudioStream(inputStream)
    }

    val monoAudioStream = convertChannels(audioStream, 1)

    val dispatcher = createAudioDispatcher(monoAudioStream, monoAudioStream.format, bufferSize)

    return TarosDspInput(dispatcher)
}
