package com.singing.api.service.publication

import com.singing.api.domain.model.Publication

interface PublicationService {
    suspend fun getAll(): List<Publication>

    suspend fun getAccountPublications(accountId: Int): List<Publication>
}
