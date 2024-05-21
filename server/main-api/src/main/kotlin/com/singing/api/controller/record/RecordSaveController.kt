package com.singing.api.controller.publication.record

import com.singing.api.security.tryAuthenticate
import com.singing.api.service.record.data.RecordDataService
import com.singing.config.track.TrackProperties
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import kotlinx.coroutines.coroutineScope
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.createDirectories


@RestController
@RequestMapping("/record")
class RecordSaveController(
    private val recordDataService: RecordDataService,
) {
    companion object {
        private val Logger = LoggerFactory.getLogger(RecordSaveController::class.java)
    }

    private val basePath: Path by lazy {
        // TODO
        Paths.get("TestStore").createDirectories()
    }

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @SecurityRequirement(name = "bearerAuth")
    suspend fun createNewRecord(
        @RequestParam("voice") voiceRecord: MultipartFile,
        @RequestParam("track", required = false) audioTrack: MultipartFile?,
    ): String = tryAuthenticate {
        coroutineScope {
            assertFileIsAcceptable(voiceRecord)

            if (audioTrack != null) {
                assertFileIsAcceptable(audioTrack)
            }

            val voiceFile = basePath.resolve(voiceRecord.originalFilename ?: "voice.raw").also {
                voiceRecord.transferTo(it)
            }

            val audioTrackFile = audioTrack?.let { file ->
                basePath.resolve(file.originalFilename ?: "audio.raw").also {
                    file.transferTo(it)
                }
            }

            Logger.debug("New file received")
            Logger.debug("Parsing...")

            // TODO
            val record = recordDataService
                .buildRecord(
                    voiceFile.toFile(),
                    audioTrackFile?.toFile(),
                )
                .also {
                    it.account = account
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
