package com.singing.app.audio.compute

import com.singing.app.audio.createTrackAudioParser
import com.singing.app.audio.createVoiceAudioParser
import com.singing.app.base.ComposeFile
import com.singing.audio.sampled.model.TimedFrequency
import com.singing.domain.model.RecordPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext

suspend fun computeRecordPointsByFiles(
    voiceFile: ComposeFile,
    trackFile: ComposeFile?,
): List<RecordPoint> = withContext(Dispatchers.Default) {
    val voiceDeferred = async(Dispatchers.IO) {
        val voiceParser = createVoiceAudioParser(
            file = voiceFile,
            filters = listOf(),
        )

        voiceParser.parse()
            .toList()
            .also { voiceParser.release() }
    }

    val trackDeferred = trackFile?.let {
        async(Dispatchers.IO) {
            val trackParser = createTrackAudioParser(
                file = it,
                filters = listOf(),
            )

            trackParser.parse()
                .toList()
                .also { trackParser.release() }
        }
    }

    val voiceParserResult = voiceDeferred.await()
    val trackParserResult = trackDeferred?.await()

    withContext(Dispatchers.Default) {
        mergeRecords(voiceParserResult, trackParserResult)
            .map {
                RecordPoint(
                    time = it.key,
                    first = it.value.first,
                    second = it.value.second,
                )
            }
    }
}

private fun mergeRecords(
    voice: List<TimedFrequency>,
    track: List<TimedFrequency>?
): Map<Long, Pair<Double?, Double?>> =
    when (track) {
        null -> voice
            .map {
                it.positionMs to (it.frequency to null)
            }
            .toList()
            .associate { it }

        else -> LinkedHashMap<Long, Pair<Double?, Double?>>(voice.size)
            .also { result ->
                for (voiceItem in voice) {
                    val trackItem = track.firstOrNull {
                        it.positionMs >= voiceItem.positionMs
                    }

                    result[voiceItem.positionMs] = voiceItem.frequency to trackItem?.frequency
                }
            }
    }
