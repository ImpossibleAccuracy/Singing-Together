package com.singing.app.data.repository

import com.singing.app.domain.model.PublicationTagStatistics
import com.singing.app.domain.repository.PublicationTagRepository

class PublicationTagRepositoryImpl : PublicationTagRepository {
    override suspend fun getPopularCategories(): List<PublicationTagStatistics> {
        TODO("Not yet implemented")
    }
}