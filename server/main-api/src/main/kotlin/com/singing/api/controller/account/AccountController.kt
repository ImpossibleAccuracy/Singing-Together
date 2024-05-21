package com.singing.api.controller.account

import com.singing.api.domain.require
import com.singing.api.service.account.AccountService
import com.singing.app.domain.dto.AccountInfoDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/account")
class AccountController(
    private val accountService: AccountService,
) {
    @GetMapping("/{id}/info")
    suspend fun getAccountInfo(@PathVariable("id") id: Int): AccountInfoDto {
        val account = accountService
            .get(id)
            .require()

        return AccountInfoDto(
            publicationsCount = accountService.getPublicationsCount(account.id!!),
            registeredAt = account.createdAt,
        )
    }
}
