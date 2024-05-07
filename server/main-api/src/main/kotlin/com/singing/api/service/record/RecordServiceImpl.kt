package com.singing.api.service.record

import com.singing.api.domain.model.Account
import com.singing.api.domain.model.Record
import com.singing.api.domain.model.RecordItem
import com.singing.api.domain.repository.RecordRepository
import com.singing.api.domain.specifications.*
import com.singing.api.security.requireAuthenticatedOrDefault
import com.singing.audio.getFileDuration
import com.singing.audio.sampled.input.toAudioParams
import com.singing.audio.sampled.model.TimedFrequency
import com.singing.audio.taros.decoder.timed.TimedTarosDspDecoder
import com.singing.audio.taros.input.openInputStreamAsTarosDspInput
import com.singing.audio.taros.parser.TarosDspParser
import com.singing.config.track.TrackProperties
import com.singing.config.voice.VoiceProperties
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import java.io.File
import java.io.InputStream
import kotlin.math.roundToInt

@Service
class RecordServiceImpl(
    private val recordRepository: RecordRepository,
) : RecordService {
    override suspend fun getPublicRecords(
        onlyPublished: Boolean
    ): List<Record> {
        if (!onlyPublished) return recordRepository.findAll()

        val onlyPublishedSpec = Record::publications.isNotEmpty()

        // Add user's own records
        val accountOwnRecordsSpec = requireAuthenticatedOrDefault(null) {
            where<Record> { root ->
                equal(
                    root.join(Record::account).get(Account::id),
                    account.id
                )
            }
        }

        return recordRepository.findAll(
            or(
                onlyPublishedSpec,
                accountOwnRecordsSpec
            )
        )
    }

    override suspend fun getAccountRecords(accountId: Int) =
        recordRepository.findByAccount_Id(accountId)

    override suspend fun buildRecord(
        voiceFile: File,
        trackFile: File?,
    ): Record = coroutineScope {
        val result = Record()

        val voiceInput = voiceFile.inputStream()
        val trackInput = trackFile?.inputStream()

        val durationDeferred = async {
            getFileDuration(voiceFile)
        }

        val voiceDeferred = async {
            parseFileToFrequencies(
                voiceInput,
                VoiceProperties.inputBufferSize
            )
        }

        val trackDeferred = trackInput?.let {
            async {
                println(8)
                parseFileToFrequencies(
                    it,
                    TrackProperties.bufferSize
                )
            }
        }

        result.duration = durationDeferred.await()

        val voiceParserResult = voiceDeferred.await()
        val trackParserResult = trackDeferred?.await()

        val merged = mergeRecords(voiceParserResult, trackParserResult)

        result.points = mapToRecordItems(merged).toSet()

        return@coroutineScope result
    }

    private suspend fun parseFileToFrequencies(
        file: InputStream,
        bufferSize: Int
    ): List<TimedFrequency> {
        val input = openInputStreamAsTarosDspInput(file, bufferSize)

        val parser = TarosDspParser(
            params = input.format.toAudioParams(bufferSize),
            input = input,
            decoder = TimedTarosDspDecoder()
        )

        return parser
            .parse()
            .flowOn(Dispatchers.IO)
            .toList()
    }

    private fun mergeRecords(
        voice: List<TimedFrequency>,
        track: List<TimedFrequency>?
    ): Map<Long, Pair<Double, Double?>> {
        if (track == null) {
            return voice
                .map {
                    it.positionMs to (it.frequency to null)
                }
                .toList()
                .associate { it }
        }

        return LinkedHashMap<Long, Pair<Double, Double?>>(voice.size)
            .also { result ->
                for (voiceItem in voice) {
                    val trackItem = track.firstOrNull {
                        it.positionMs >= voiceItem.positionMs
                    }

                    result[voiceItem.positionMs] = voiceItem.frequency to trackItem?.frequency
                }
            }
    }

    private fun mapToRecordItems(merged: Map<Long, Pair<Double, Double?>>) =
        merged
            .map {
                RecordItem(
                    time = it.key,
                    frequency = it.value.first.roundToInt(),
                    trackFrequency = it.value.second?.roundToInt()
                )
            }
}
