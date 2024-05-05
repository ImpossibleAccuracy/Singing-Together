package com.singing.api.controller.publication

import com.singing.api.domain.pojo.PublicationDto
import com.singing.api.enums.Privileges
import com.singing.api.security.requireAuthenticated
import com.singing.api.service.publication.PublicationService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/publication")
class PublicationController(
    private val publicationService: PublicationService,
) {
    @GetMapping
    suspend fun listPublications(): List<PublicationDto> =
        requireAuthenticated {
            val items = when (hasAnyPrivilege(Privileges.ReadPublications)) {
                true -> publicationService.getAll()

                false -> publicationService.getAccountPublications(
                    accountId = account.id!!
                )
            }

            items.map {
                PublicationDto.fromModel(it)
            }
        }
}
