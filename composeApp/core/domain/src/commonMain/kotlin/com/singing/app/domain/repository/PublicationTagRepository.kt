package com.singing.app.domain.repository

import com.singing.app.domain.model.DataState
import com.singing.app.domain.model.PublicationTagStatistics
import kotlinx.coroutines.flow.Flow

interface PublicationTagRepository {
    fun getPopularTags(): Flow<DataState<List<PublicationTagStatistics>>>
}
