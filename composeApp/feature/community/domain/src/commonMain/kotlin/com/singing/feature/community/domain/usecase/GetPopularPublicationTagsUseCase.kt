package com.singing.feature.community.domain.usecase

import com.singing.app.domain.model.DataState
import com.singing.app.domain.model.PublicationTagStatistics
import com.singing.app.domain.model.map
import com.singing.app.domain.repository.PublicationTagRepository
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetPopularPublicationTagsUseCase(
    private val publicationTagRepository: PublicationTagRepository
) {
    operator fun invoke(): Flow<DataState<PersistentList<PublicationTagStatistics>>> = flow {
        emit(DataState.Loading)

        emit(publicationTagRepository.getPopularTags().map { it.toPersistentList() })
    }
}
