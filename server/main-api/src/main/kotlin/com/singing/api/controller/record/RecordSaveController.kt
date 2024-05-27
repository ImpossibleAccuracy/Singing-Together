package com.singing.api.controller.record

import com.singing.api.domain.exception.ParsingCancellationException
import com.singing.api.domain.model.AccountEntity
import com.singing.api.domain.model.RecordEntity
import com.singing.api.domain.toDto
import com.singing.api.security.requireAuthenticated
import com.singing.api.service.record.RecordService
import com.singing.api.service.record.data.RecordDataService
import com.singing.api.service.storage.FileStorageService
import com.singing.api.service.storage.StorageCatalog
import com.singing.config.track.TrackProperties
import com.singing.domain.payload.dto.RecordDto
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import kotlinx.coroutines.*
import org.hibernate.validator.constraints.Length
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.io.File


@RestController
@RequestMapping("/record")
class RecordSaveController(
    private val fileStorageService: FileStorageService,
    private val recordDataService: RecordDataService,
    private val recordService: RecordService,
) {
    companion object {
        private val Logger = LoggerFactory.getLogger(RecordSaveController::class.java)
    }

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @SecurityRequirement(name = "bearerAuth")
    suspend fun createNewRecord(
        @RequestParam("title", required = false) @Length(max = 255) title: String?,
        @RequestParam("voice") voiceRecord: MultipartFile,
        @RequestParam("track", required = false) audioTrack: MultipartFile?,
    ): RecordDto = requireAuthenticated {
        assertFileIsAcceptable(voiceRecord)

        if (audioTrack != null) {
            assertFileIsAcceptable(audioTrack)
        }

        Logger.debug("New file received")

        val voiceFile = withContext(Dispatchers.IO) {
            fileStorageService.store(voiceRecord, StorageCatalog.Temp)
        }

        val trackFile = audioTrack?.let {
            withContext(Dispatchers.IO) {
                fileStorageService.store(audioTrack, StorageCatalog.Temp)
            }
        }

        try {
            val resultRecord = supervisorScope {
                collectFileData(
                    account = account,
                    title = title,
                    voiceFile = voiceFile,
                    trackFile = trackFile,
                )
            }

            resultRecord
                .let {
                    recordService.save(it)
                }
                .toDto()
        } catch (e: ParsingCancellationException) {
            Logger.debug("Parse timeout: ${e.message}")

            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Parsing took too long")
        } catch (e: Throwable) {
            Logger.debug("Parse failed: ${e.message}")
            e.printStackTrace()

            fileStorageService.delete(voiceFile, StorageCatalog.Temp)

            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Parse failed")
        } finally {
            fileStorageService.delete(voiceFile, StorageCatalog.Temp)

            if (trackFile != null) {
                fileStorageService.delete(trackFile, StorageCatalog.Temp)
            }
        }
    }

    private suspend fun collectFileData(
        account: AccountEntity,
        title: String?,
        voiceFile: File,
        trackFile: File?,
    ): RecordEntity = coroutineScope {
        Logger.debug("Parsing...")

        val recordPointsDeferred = async {
            recordDataService.buildRecord(
                voiceFile,
                trackFile,
            )
        }

        val recordDurationDeferred = async {
            recordDataService.getDuration(voiceFile)
        }

        val recordDuration = recordDurationDeferred.await()
        val recordPoints = recordPointsDeferred.await()

        val record = RecordEntity().also {
            it.title = title
            it.author = account
            it.duration = recordDuration
            it.points = recordPoints
                .onEach { point ->
                    point.record = it
                }
                .toSet()
        }

        val recordAccuracyDeferred = trackFile?.let {
            async {
                recordDataService.computeAccuracy(recordPoints)
                    .also {
                        Logger.debug("Parse done!")
                    }
            }
        }

        System.gc() // idk, how it is works, but without this will be error

        val voiceDocument = withContext(Dispatchers.IO) {
            fileStorageService.findDocumentOrCreate(voiceFile)
        }

        val trackDocument = trackFile?.let {
            withContext(Dispatchers.IO) {
                fileStorageService.findDocumentOrCreate(it)
            }
        }

        val recordAccuracy = recordAccuracyDeferred?.await()

        record.also {
            it.accuracy = recordAccuracy
            it.voiceRecord = voiceDocument
            it.trackRecord = trackDocument
        }
    }

    private fun assertFileIsAcceptable(file: MultipartFile) {
        if (file.isEmpty) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "File cannot be empty")
        }

        if (file.contentType == null || file.contentType!! !in TrackProperties.allowedSoundFormatsMimeType) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "File of unacceptable type")
        }
    }
}
