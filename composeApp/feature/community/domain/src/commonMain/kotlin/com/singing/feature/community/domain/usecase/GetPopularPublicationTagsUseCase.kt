package com.singing.feature.community.domain.usecase

import com.singing.app.domain.model.DataState
import com.singing.app.domain.model.PublicationTagStatistics
import com.singing.app.domain.model.mapData
import com.singing.app.domain.repository.PublicationTagRepository
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetPopularPublicationTagsUseCase(
    private val publicationTagRepository: PublicationTagRepository
) {
    operator fun invoke(): Flow<DataState<PersistentList<PublicationTagStatistics>>> = publicationTagRepository
        .getPopularTags()
        .map { state ->
            state.mapData {
                it.toPersistentList()
            }
        }
}
