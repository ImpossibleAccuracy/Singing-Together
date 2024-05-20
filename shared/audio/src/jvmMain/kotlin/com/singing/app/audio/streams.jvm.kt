package com.singing.app.audio

import com.singing.audio.library.filter.AudioFilter
import com.singing.audio.library.model.AudioParams
import com.singing.audio.library.parser.AudioParser
import com.singing.audio.sampled.input.toAudioParams
import com.singing.audio.sampled.model.TimedFrequency
import com.singing.audio.taros.decoder.TarosDspDecoderImpl
import com.singing.audio.taros.decoder.timed.TimedTarosDspDecoder
import com.singing.audio.taros.filter.TarosAudioFilter
import com.singing.audio.taros.input.TarosDspInput
import com.singing.audio.taros.input.createMicrophoneInput
import com.singing.audio.taros.input.openInputStreamAsTarosDspInput
import com.singing.audio.taros.parser.TarosDspParser
import com.singing.audio.utils.ComposeFile
import com.singing.config.track.TrackProperties
import com.singing.config.voice.VoiceProperties
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


val microphoneInput: TarosDspInput by lazy {
    createMicrophoneInput(
        VoiceProperties.inputSampleRate,
        VoiceProperties.inputBufferSize,
    )
}

actual suspend fun createVoiceAudioParser(filters: List<AudioFilter>): AudioParser<Double>? {
    val input = withContext(Dispatchers.IO) {
        microphoneInput
    }

    val format = input.format
    val params = AudioParams(
        bufferSize = VoiceProperties.inputBufferSize,
        frameRate = format.frameRate,
        frameSize = format.frameSize,
        sampleRate = format.sampleRate,
        sampleSizeInBits = format.sampleSizeInBits,
        channels = format.channels,
        isBigEndian = format.isBigEndian,
    )

    val tarosFilters = filters.map {
        if (it is TarosAudioFilter) it
        else throw IllegalArgumentException("TarosDspParser accepts only filters of \"TarosAudioFilter\" type")
    }

    return TarosDspParser(
        params = params,
        input = input,
        decoder = TarosDspDecoderImpl(),
        filters = tarosFilters,
    )
}

actual suspend fun createVoiceAudioParser(
    file: ComposeFile,
    filters: List<AudioFilter>
): AudioParser<TimedFrequency> = createTrackAudioParser(file, filters)

actual suspend fun createTrackAudioParser(
    file: ComposeFile,
    filters: List<AudioFilter>
): AudioParser<TimedFrequency> {
    val input = withContext(Dispatchers.IO) {
        openInputStreamAsTarosDspInput(
            inputStream = file.inputStream(),
            bufferSize = TrackProperties.bufferSize,
        )
    }

    val tarosFilters = filters.map {
        if (it is TarosAudioFilter) it
        else throw IllegalArgumentException("TarosDspParser accepts only filters of \"TarosAudioFilter\" type")
    }

    return TarosDspParser(
        params = input.format.toAudioParams(TrackProperties.bufferSize),
        input = input,
        decoder = TimedTarosDspDecoder(),
        filters = tarosFilters,
    )
}
