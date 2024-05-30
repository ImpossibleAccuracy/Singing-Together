package com.singing.app.domain.repository

import com.singing.app.domain.model.DataState
import com.singing.app.domain.model.PublicationTagStatistics

interface PublicationTagRepository {
    suspend fun getPopularTags(): DataState<List<PublicationTagStatistics>>
}
