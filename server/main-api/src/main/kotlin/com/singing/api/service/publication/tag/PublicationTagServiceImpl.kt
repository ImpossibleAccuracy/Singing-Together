package com.singing.api.service.publication.tag

import com.singing.api.domain.model.CategoryInfoEntity
import com.singing.api.domain.model.PublicationTagEntity
import com.singing.api.domain.repository.CategoryInfoRepository
import com.singing.api.domain.repository.PublicationTagRepository
import com.singing.api.domain.repository.pagination.OffsetBasedPageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class PublicationTagServiceImpl(
    private val publicationTagRepository: PublicationTagRepository,
    private val categoryInfoRepository: CategoryInfoRepository,
) : PublicationTagService {
    override suspend fun createAll(tags: List<String>): List<PublicationTagEntity> {
        val entities = tags.map {
            PublicationTagEntity(
                title = it,
            )
        }

        return publicationTagRepository.saveAll(entities)
    }

    override suspend fun getPopularCategories(): List<CategoryInfoEntity> {
        val pagination = OffsetBasedPageRequest(
            offset = 0,
            limit = 10,
            sort = Sort.by(Sort.Direction.DESC, CategoryInfoEntity::publications.name)
        )

        return categoryInfoRepository
            .findAll(pagination)
            .toList()
    }
}
