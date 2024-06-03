package com.singing.api.controller.account

import com.singing.api.domain.exception.ResourceNotFoundException
import com.singing.api.domain.makeAccountInfoDto
import com.singing.api.domain.require
import com.singing.api.domain.toDto
import com.singing.api.service.account.AccountService
import com.singing.domain.payload.dto.AccountDto
import com.singing.domain.payload.dto.AccountInfoDto
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/account")
class AccountController(
    private val accountService: AccountService,
) {
    @GetMapping("/{id}")
    suspend fun getAccount(@PathVariable("id") id: Int): AccountDto = accountService
        .get(id)
        .require()
        .toDto()

    @GetMapping("/username")
    suspend fun getByUsername(@RequestParam("username") username: String): AccountDto =
        accountService
            .getByUsername(username)
            .orElseThrow {
                ResourceNotFoundException("Account not found")
            }
            .toDto()

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
