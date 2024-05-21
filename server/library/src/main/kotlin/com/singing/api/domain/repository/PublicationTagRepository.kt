package com.singing.api.domain.repository

import com.singing.api.domain.model.PublicationTagEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface PublicationTagRepository : JpaRepository<PublicationTagEntity, Int>,
    JpaSpecificationExecutor<PublicationTagEntity>