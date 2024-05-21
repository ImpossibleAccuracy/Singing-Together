package com.singing.api.service.publication.tag

import com.singing.api.domain.model.CategoryInfoEntity
import com.singing.api.domain.model.PublicationTagEntity

interface PublicationTagService {
    suspend fun createAll(tags: List<String>): List<PublicationTagEntity>

    suspend fun getPopularCategories(): List<CategoryInfoEntity>
}
