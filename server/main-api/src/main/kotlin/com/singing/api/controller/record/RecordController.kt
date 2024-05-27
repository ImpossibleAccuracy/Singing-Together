package com.singing.api.controller.record

import com.singing.api.domain.model.DocumentEntity
import com.singing.api.domain.model.RecordEntity
import com.singing.api.domain.model.RecordItemEntity
import com.singing.api.domain.require
import com.singing.api.domain.secureDelete
import com.singing.api.domain.secureRead
import com.singing.api.domain.toDto
import com.singing.api.security.requireAuthenticated
import com.singing.api.security.tryAuthenticate
import com.singing.api.service.record.RecordService
import com.singing.api.service.record.data.RecordDataService
import com.singing.domain.payload.dto.RecordDto
import com.singing.domain.payload.dto.RecordPointDto
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.io.File
import java.net.URLConnection


@RestController
@RequestMapping("/record")
class RecordController(
    private val recordService: RecordService,
    private val recordDataService: RecordDataService,
) {
    @GetMapping("/my")
    @SecurityRequirement(name = "bearerAuth")
    suspend fun accountRecords(): List<RecordDto> = requireAuthenticated {
        recordService
            .accountRecords(accountId = account.id!!)
            .map(RecordEntity::toDto)
    }

    @GetMapping("/{id}/points")
    @SecurityRequirement(name = "bearerAuth")
    suspend fun getRecordPoints(@PathVariable id: Int): List<RecordPointDto> = tryAuthenticate {
        val record = recordService.get(id).require()

        secureRead(record)

        val items = recordDataService.recordPoints(record.id!!)

        items.map(RecordItemEntity::toDto)
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    suspend fun deleteRecord(@PathVariable id: Int): Unit = requireAuthenticated {
        val record = recordService.get(id).require()

        secureDelete(record)

        recordService.delete(record.id!!)
    }

    @GetMapping("/{id}/voice")
    @SecurityRequirement(name = "bearerAuth")
    suspend fun getRecordVoiceFile(
        @PathVariable id: Int,
        response: HttpServletResponse,
    ): Unit = tryAuthenticate {
        val record = recordService.get(id).require()

        secureRead(record)

        val document = recordDataService.loadRecordVoiceFile(record.id!!)

        sendResponse(document, response)
    }

    @GetMapping("/{id}/track")
    @SecurityRequirement(name = "bearerAuth")
    suspend fun getRecordTrackFile(
        @PathVariable id: Int,
        response: HttpServletResponse,
    ) = tryAuthenticate {
        val record = recordService.get(id).require()

        secureRead(record)

        val document = recordDataService
            .loadRecordTrackFile(record.id!!)
            .require("No track found for Record#${id}")

        sendResponse(document, response)
    }

    private fun sendResponse(
        document: DocumentEntity,
        response: HttpServletResponse,
    ) {
        val file = File(document.path!!)

        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${file.name}")
        response.addHeader(HttpHeaders.CONTENT_TYPE, getFileMimeType(document))
        response.addHeader(HttpHeaders.CONTENT_LENGTH, file.length().toString())

        response.addHeader(HttpHeaders.PRAGMA, "public")
        response.addHeader(HttpHeaders.EXPIRES, "0")
        response.addHeader(HttpHeaders.CACHE_CONTROL, "must-revalidate, post-check=0, pre-check=0")
        response.addHeader(HttpHeaders.CACHE_CONTROL, "public")

        file.inputStream().copyTo(response.outputStream)
    }

    private fun getFileMimeType(document: DocumentEntity): String =
        document.type?.mimeType
            ?: URLConnection.guessContentTypeFromName(document.title!!)
            ?: MediaType.TEXT_PLAIN_VALUE
}
