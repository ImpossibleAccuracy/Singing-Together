package com.singing.api.service.record.data

import com.singing.api.domain.exception.ParsingCancellationException
import com.singing.api.domain.model.DocumentEntity
import com.singing.api.domain.model.RecordItemEntity
import com.singing.api.domain.repository.DocumentRepository
import com.singing.api.domain.repository.RecordItemRepository
import com.singing.app.audio.createTrackAudioParser
import com.singing.app.audio.createVoiceAudioParser
import com.singing.app.audio.getFileDuration
import com.singing.app.base.ComposeFile
import com.singing.audio.sampled.model.TimedFrequency
import com.singing.domain.model.PointAccuracy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withTimeout
import org.springframework.stereotype.Service
import java.io.File
import java.util.Optional
import kotlin.math.abs
import kotlin.time.toKotlinDuration

@Service
class RecordDataServiceImpl(
    private val recordPointRepository: RecordItemRepository,
    private val properties: RecordProcessingProperties,
    private val documentRepository: DocumentRepository,
) : RecordDataService {
    override suspend fun getDuration(file: File): Long {
        return getFileDuration(file)
    }

    override suspend fun computeAccuracy(points: List<RecordItemEntity>): Double {
        // TODO: extract algorithm
        val total = points.sumOf {
            val diff = abs(it.frequency!! - it.trackFrequency!!)

            PointAccuracy.calculateAccuracy(diff.toFloat()).percent.toInt()
        }

        return total.toDouble() / points.size
    }

    override suspend fun buildRecord(
        voiceFile: File,
        trackFile: File?,
    ): List<RecordItemEntity> = try {
        withTimeout(properties.maxParsingDuration.toKotlinDuration()) {
            val voiceDeferred = async(Dispatchers.IO) {
                val voiceParser = createVoiceAudioParser(
                    file = ComposeFile(voiceFile),
                    filters = listOf(),
                )

                voiceParser.parse()
                    .toList()
                    .also { voiceParser.release() }
            }

            val trackDeferred = trackFile?.let {
                async(Dispatchers.IO) {
                    val trackParser = createTrackAudioParser(
                        file = ComposeFile(it),
                        filters = listOf(),
                    )

                    trackParser.parse()
                        .toList()
                        .also { trackParser.release() }
                }
            }

            val voiceParserResult = voiceDeferred.await()
            val trackParserResult = trackDeferred?.await()

            val merged = mergeRecords(voiceParserResult, trackParserResult)

            mapToRecordItems(merged)
        }
    } catch (e: TimeoutCancellationException) {
        throw ParsingCancellationException(e)
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
                RecordItemEntity(
                    time = it.key,
                    frequency = it.value.first,
                    trackFrequency = it.value.second,
                )
            }

    override suspend fun recordPoints(recordId: Int, page: Int): List<RecordItemEntity> = TODO()
//        recordPointRepository.findByRecord_IdOrderByTimeAsc(recordId)

    override suspend fun loadRecordVoiceFile(recordId: Int): DocumentEntity =
        documentRepository.findByVoiceRecordRecords_Id(recordId)
            .orElseThrow()

    override suspend fun loadRecordTrackFile(recordId: Int): Optional<DocumentEntity> =
        documentRepository.findByTrackRecords_Id(recordId)
}
