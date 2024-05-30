package com.singing.app.data.repository

import com.singing.app.data.datasource.declaration.PublicationDataSource
import com.singing.app.domain.model.DataState
import com.singing.app.domain.model.PublicationTagStatistics
import com.singing.app.domain.repository.PublicationTagRepository

class PublicationTagRepositoryImpl(
    private val dataSource: PublicationDataSource.Remote,
) : PublicationTagRepository {
    override suspend fun getPopularTags(): DataState<List<PublicationTagStatistics>> =
        dataSource.fetchPopularTags().asDataState
}