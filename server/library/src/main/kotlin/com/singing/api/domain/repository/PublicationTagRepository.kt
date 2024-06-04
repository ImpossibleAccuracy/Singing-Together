package com.singing.api.domain.repository

import com.singing.api.domain.model.PublicationTagEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface PublicationTagRepository : JpaRepository<PublicationTagEntity, Int>,
    JpaSpecificationExecutor<PublicationTagEntity> {

    fun findByTitle(title: String): Optional<PublicationTagEntity>
}