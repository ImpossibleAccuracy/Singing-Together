package com.singing.api.service.record.data

import com.singing.api.domain.model.DocumentEntity
import com.singing.api.domain.model.DocumentTypeEntity
import com.singing.api.domain.model.RecordEntity
import com.singing.api.domain.model.RecordItemEntity
import com.singing.api.domain.repository.RecordItemRepository
import com.singing.app.audio.createTrackAudioParser
import com.singing.app.audio.createVoiceAudioParser
import com.singing.app.audio.getFileDuration
import com.singing.audio.sampled.model.TimedFrequency
import com.singing.audio.utils.ComposeFile
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service
import java.io.File
import java.time.Instant
import java.util.*

@Service
class RecordDataServiceImpl(
    private val recordPointRepository: RecordItemRepository,
) : RecordDataService {
    override suspend fun buildRecord(
        voiceFile: File,
        trackFile: File?,
    ): RecordEntity = coroutineScope {
        val result = RecordEntity()

        val durationDeferred = async {
            getFileDuration(voiceFile)
        }

        val voiceDeferred = async {
            val parser = createVoiceAudioParser(
                ComposeFile(voiceFile),
                filters = listOf(),
            )

            parser.parse().toList()
        }

        val trackDeferred = trackFile?.let {
            async {
                val parser = createTrackAudioParser(
                    ComposeFile(it),
                    filters = listOf(),
                )

                parser.parse().toList()
            }
        }

        result.duration = durationDeferred.await()

        val voiceParserResult = voiceDeferred.await()
        val trackParserResult = trackDeferred?.await()

        val merged = mergeRecords(voiceParserResult, trackParserResult)

        result.points = mapToRecordItems(merged).toSet()

        return@coroutineScope result
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

    override suspend fun recordPoints(recordId: Int): List<RecordItemEntity> =
        recordPointRepository.findByRecord_IdOrderByTimeAsc(recordId)

    override suspend fun loadRecordVoiceFile(recordId: Int): DocumentEntity {
        val file =
            File("C:\\Users\\Gamer\\Desktop\\audio_samples\\Gales-of-Song-English-Version-_Studio-Quality-AcapellaVocals-Only_-_Belle_.wav")

        return DocumentEntity(
            id = 1,
            createdAt = Instant.now(),
            title = file.nameWithoutExtension,
            hash = "",
            path = file.absolutePath,
            type = DocumentTypeEntity(
                id = 1,
                title = "WAV audio",
                mimeType = "audio/wave"
            )
        )
    }

    override suspend fun loadRecordTrackFile(recordId: Int): Optional<DocumentEntity> {
        TODO("Not yet implemented")
    }
}
