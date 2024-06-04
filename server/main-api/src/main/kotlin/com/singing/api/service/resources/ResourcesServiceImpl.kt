package com.singing.api.service.resources

import com.singing.api.domain.model.AccountEntity
import org.springframework.stereotype.Service

@Service
class ResourcesServiceImpl(
    private val resourcesProperties: ResourcesProperties,
) : ResourcesService {
    override suspend fun getAccountAvatar(account: AccountEntity): String? {
        if (account.avatar == null) return null

        return "${resourcesProperties.resourcesServerUrl}/account/${account.id}/avatar"
    }
}