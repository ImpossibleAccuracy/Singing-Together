package com.singing.api.service.resources

import com.singing.api.domain.model.AccountEntity

interface ResourcesService {
    suspend fun getAccountAvatar(account: AccountEntity): String?
}