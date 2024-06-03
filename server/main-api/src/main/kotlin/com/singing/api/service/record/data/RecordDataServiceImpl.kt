package com.singing.api.service.record.data

import com.singing.api.constants.Pagination
import com.singing.api.domain.exception.ParsingCancellationException
import com.singing.api.domain.model.DocumentEntity
import com.singing.api.domain.model.RecordItemEntity
import com.singing.api.domain.repository.DocumentRepository
import com.singing.api.domain.repository.RecordItemRepository
import com.singing.api.domain.repository.pagination.OffsetBasedPageRequest
import com.singing.app.audio.compute.computeAccuracyByPoints
import com.singing.app.audio.compute.computeRecordPointsByFiles
import com.singing.app.audio.getFileDuration
import com.singing.app.base.ComposeFile
import com.singing.domain.model.RecordPoint
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.io.File
import java.util.*
import kotlin.time.toKotlinDuration

@Service
class RecordDataServiceImpl(
    private val recordPointRepository: RecordItemRepository,
    private val properties: RecordProcessingProperties,
    private val documentRepository: DocumentRepository,
) : RecordDataService {
    override suspend fun getDuration(file: File): Long {
        return getFileDuration(ComposeFile(file))
    }

    override suspend fun computeAccuracy(points: List<RecordItemEntity>): Double =
        computeAccuracyByPoints(
            points.map {
                RecordPoint(
                    time = it.time!!,
                    first = it.frequency,
                    second = it.trackFrequency,
                )
            }
        )

    override suspend fun buildRecord(
        voiceFile: File,
        trackFile: File?,
    ): List<RecordItemEntity> = try {
        withTimeout(properties.maxParsingDuration.toKotlinDuration()) {
            computeRecordPointsByFiles(
                ComposeFile(voiceFile),
                trackFile?.let { ComposeFile(it) }
            ).map {
                RecordItemEntity(
                    time = it.time,
                    frequency = it.first,
                    trackFrequency = it.second,
                )
            }
        }
    } catch (e: TimeoutCancellationException) {
        throw ParsingCancellationException(e)
    }


    override suspend fun recordPoints(recordId: Int, page: Int): List<RecordItemEntity> =
        recordPointRepository.findByRecord_IdOrderByTimeAsc(
            id = recordId,
            pageable = OffsetBasedPageRequest(
                (Pagination.RECORD_POINT_PAGE_SIZE * page).toLong(),
                Pagination.RECORD_POINT_PAGE_SIZE,
                Sort.by(RecordItemEntity::time.name)
            )
        )

    override suspend fun loadRecordVoiceFile(recordId: Int): DocumentEntity =
        documentRepository.findByVoiceRecordRecords_Id(recordId)
            .orElseThrow()

    override suspend fun loadRecordTrackFile(recordId: Int): Optional<DocumentEntity> =
        documentRepository.findByTrackRecords_Id(recordId)
}
