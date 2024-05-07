package com.singing.api.controller.record

import com.singing.api.domain.pojo.RecordDto
import com.singing.api.enums.Privileges
import com.singing.api.security.requireAuthenticated
import com.singing.api.security.tryAuthenticate
import com.singing.api.service.record.RecordService
import com.singing.config.track.TrackProperties
import kotlinx.coroutines.coroutineScope
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.createDirectories


@RestController
@RequestMapping("/record")
class RecordController(
    private val recordService: RecordService,
) {
    companion object {
        private val Logger = LoggerFactory.getLogger(RecordController::class.java)
    }

    private val basePath: Path by lazy {
        Paths.get("TestStore").createDirectories()
    }

    @GetMapping
    suspend fun getAvailableRecords(): List<RecordDto> = tryAuthenticate {
        val canReadAllRecords = hasAnyPrivilege(Privileges.ReadRecords)

        recordService
            .getPublicRecords(
                onlyPublished = !canReadAllRecords
            )
            .map {
                RecordDto.fromModel(it)
            }
    }

    @GetMapping("/my")
    suspend fun accountRecords(): List<RecordDto> = requireAuthenticated {
        recordService
            .getAccountRecords(
                accountId = account.id!!
            )
            .map {
                RecordDto.fromModel(it)
            }
    }

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    suspend fun createNewRecord(
        @RequestParam("voice") voiceRecord: MultipartFile,
        @RequestParam("track", required = false) audioTrack: MultipartFile?,
    ): String = requireAuthenticated {
        coroutineScope {
            assertFileIsAcceptable(voiceRecord)

            if (audioTrack != null) {
                assertFileIsAcceptable(audioTrack)
            }

            val voiceFile = basePath.resolve(voiceRecord.originalFilename ?: "voice.raw").also {
                voiceRecord.transferTo(it)
            }

            val audioTrackFile = audioTrack?.let { file ->
                basePath.resolve(file.originalFilename ?: "voice.raw").also {
                    file.transferTo(it)
                }
            }

            Logger.debug("New file received")
            Logger.debug("Parsing...")

            val record = recordService
                .buildRecord(
                    voiceFile.toFile(),
                    audioTrackFile?.toFile(),
                )
                .apply {
                    this.account = this@requireAuthenticated.account
                }

            Logger.debug("Parse done!")

            "Success"
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
