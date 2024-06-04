package com.singing.api.controller.publication

import com.singing.api.domain.exception.ResourceAccessDeniedException
import com.singing.api.domain.exception.ResourceNotFoundException
import com.singing.api.domain.model.CategoryInfoEntity
import com.singing.api.domain.model.PublicationEntity
import com.singing.api.domain.require
import com.singing.api.domain.secureDelete
import com.singing.api.domain.secureWrite
import com.singing.api.domain.toDto
import com.singing.api.security.requireAuthenticated
import com.singing.api.service.account.AccountService
import com.singing.api.service.publication.PublicationService
import com.singing.api.service.publication.tag.PublicationTagService
import com.singing.api.service.record.RecordService
import com.singing.api.service.resources.ResourcesService
import com.singing.domain.model.PublicationSort
import com.singing.domain.payload.dto.CategoryInfoDto
import com.singing.domain.payload.dto.PublicationDto
import com.singing.domain.payload.request.PublicationSearchRequest
import com.singing.domain.payload.request.PublishRecordRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/publication")
class PublicationController(
    private val accountService: AccountService,
    private val recordService: RecordService,
    private val publicationService: PublicationService,
    private val publicationTagService: PublicationTagService,
    private val resourcesService: ResourcesService,
) {
    companion object {
        private const val PUBLICATION_LATEST_DAYS = 2L
    }

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    suspend fun publishRecord(
        @RequestBody body: PublishRecordRequest
    ): PublicationDto = requireAuthenticated {
        val record = recordService
            .get(body.recordId)
            .require()

        secureWrite(record)

        if (publicationService.isPublished(record)) {
            throw ResourceAccessDeniedException("Record already published")
        }

        publicationService
            .publishRecord(
                record = record,
                account = account,
                description = body.description,
                tags = body.tags.distinct(),
            )
            .let {
                it.toDto(resourcesService.getAccountAvatar(it.account!!))
            }
    }

    @GetMapping("/account/{id}")
    suspend fun accountPublications(
        @PathVariable id: Int,
        @RequestParam("page", required = false) page: Int = 0,
        @RequestParam("sort", required = false) sort: PublicationSort = PublicationSort.DateCreated,
    ): List<PublicationDto> {
        val account = accountService.get(id).require()

        return publicationService
            .byAccount(
                accountId = account.id!!,
                page = page,
                sort = sort,
            )
            .map {
                it.toDto(resourcesService.getAccountAvatar(it.account!!))
            }
    }

    @GetMapping
    @Operation(
        parameters = [
            Parameter(
                name = "page",
                required = false,
                `in` = ParameterIn.QUERY,
                schema = Schema(type = "integer")
            ),
            Parameter(
                name = "tags",
                required = false,
                `in` = ParameterIn.QUERY,
                schema = Schema(type = "array")
            ),
            Parameter(
                name = "description",
                required = false,
                `in` = ParameterIn.QUERY,
                schema = Schema(type = "string")
            ),
            Parameter(
                name = "showOwnPublications",
                required = false,
                `in` = ParameterIn.QUERY,
                schema = Schema(type = "boolean")
            ),
            Parameter(
                name = "sort", required = false, `in` = ParameterIn.QUERY,
                schema = Schema(
                    type = "string",
                    defaultValue = "DateCreated",
                    implementation = PublicationSort::class,
                )
            ),
        ]
    )
    suspend fun search(
        @Parameter(hidden = true) body: PublicationSearchRequest,
    ): List<PublicationDto> {
        val sort = PublicationSort.entries.firstOrNull { it.name == body.sort }
            ?: throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Sort must be one of ${PublicationSort.entries.joinToString(", ")}"
            )

        return publicationService
            .search(
                page = body.page,
                tags = body.tags?.split(";"),
                description = body.description,
                showOwnPublications = body.showOwnPublications,
                sort = sort,
            )
            .map {
                it.toDto(resourcesService.getAccountAvatar(it.account!!))
            }
    }


    @GetMapping("/record/{id}")
    suspend fun getRecordPublication(@PathVariable("id") recordId: Int): PublicationDto {
        val record = recordService.get(recordId).require()

        return publicationService
            .byRecord(record.id!!)
            .orElseThrow { ResourceNotFoundException("Records#$recordId not published") }
            .let {
                it.toDto(resourcesService.getAccountAvatar(it.account!!))
            }
    }

    @GetMapping("/random")
    suspend fun getRandomPublication(): PublicationDto? =
        (publicationService.random(PUBLICATION_LATEST_DAYS) ?: publicationService.random(null))
            ?.let {
                it.toDto(resourcesService.getAccountAvatar(it.account!!))
            }

    @GetMapping("/tags")
    suspend fun getPopularCategories(): List<CategoryInfoDto> =
        publicationTagService
            .getPopularCategories()
            .map(CategoryInfoEntity::toDto)

    @DeleteMapping("/{id}")
    suspend fun delete(@PathVariable id: Int): Unit = requireAuthenticated {
        val publication = publicationService.get(id).require()

        secureDelete(publication)

        publicationService.delete(publication.id!!)
    }
}
