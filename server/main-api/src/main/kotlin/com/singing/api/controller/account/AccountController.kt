package com.singing.api.controller.account

import com.singing.api.domain.exception.ResourceNotFoundException
import com.singing.api.domain.makeAccountInfoDto
import com.singing.api.domain.require
import com.singing.api.domain.toDto
import com.singing.api.service.account.AccountService
import com.singing.api.service.resources.ResourcesService
import com.singing.domain.payload.dto.AccountDto
import com.singing.domain.payload.dto.AccountInfoDto
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import java.io.File
import java.net.URLConnection

@RestController
@RequestMapping("/account")
class AccountController(
    private val accountService: AccountService,
    private val resourcesService: ResourcesService,
) {
    @GetMapping("/{id}")
    suspend fun getAccount(@PathVariable("id") id: Int): AccountDto = accountService
        .get(id)
        .require()
        .let {
            it.toDto(resourcesService.getAccountAvatar(it))
        }

    @GetMapping("/{id}/avatar")
    suspend fun getAvatar(
        @PathVariable("id") id: Int,
        response: HttpServletResponse,
    ) {
        accountService
            .getAvatar(id)
            .require()
            .let {
                val file = File(it.path!!)

                response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=${file.name}")
                response.addHeader(HttpHeaders.CONTENT_LENGTH, file.length().toString())

                val mimeType = it.type?.mimeType
                    ?: URLConnection.guessContentTypeFromName(it.title!!)
                    ?: MediaType.IMAGE_PNG_VALUE

                response.addHeader(HttpHeaders.CONTENT_TYPE, mimeType)

                file.inputStream().copyTo(response.outputStream)
            }
    }

    @GetMapping("/username")
    suspend fun getByUsername(@RequestParam("username") username: String): AccountDto =
        accountService
            .getByUsername(username)
            .orElseThrow {
                ResourceNotFoundException("Account not found")
            }
            .let {
                it.toDto(resourcesService.getAccountAvatar(it))
            }

    @GetMapping("/{id}/info")
    suspend fun getAccountInfo(@PathVariable("id") id: Int): AccountInfoDto {
        val account = accountService
            .get(id)
            .require()

        val publicationsCount = accountService.getPublicationsCount(account.id!!)

        return makeAccountInfoDto(
            publicationsCount = publicationsCount,
            registeredAt = account.createdAt,
        )
    }
}
