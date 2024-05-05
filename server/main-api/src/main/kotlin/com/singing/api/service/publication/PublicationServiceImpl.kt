package com.singing.api.service.publication

import com.singing.api.domain.model.Publication
import com.singing.api.domain.repository.PublicationRepository
import org.springframework.stereotype.Service

@Service
class PublicationServiceImpl(
    private val publicationRepository: PublicationRepository
) : PublicationService {
    override suspend fun getAll(): List<Publication> =
        publicationRepository.findAll()

    override suspend fun getAccountPublications(accountId: Int): List<Publication> =
        publicationRepository.findByAccount_Id(accountId)
}
