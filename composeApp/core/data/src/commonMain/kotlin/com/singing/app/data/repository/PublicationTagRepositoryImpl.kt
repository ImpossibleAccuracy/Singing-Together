package com.singing.app.data.repository

import com.singing.app.domain.model.DataState
import com.singing.app.domain.model.PublicationTagStatistics
import com.singing.app.domain.repository.PublicationTagRepository
import kotlinx.coroutines.flow.Flow

class PublicationTagRepositoryImpl : PublicationTagRepository {
    override fun getPopularTags(): Flow<DataState<List<PublicationTagStatistics>>> {
        TODO("Not yet implemented")
    }
}