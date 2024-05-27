package com.singing.app.domain.repository

import com.singing.app.domain.model.PublicationTagStatistics

interface PublicationTagRepository {
    suspend fun getPopularCategories(): List<PublicationTagStatistics>
}
