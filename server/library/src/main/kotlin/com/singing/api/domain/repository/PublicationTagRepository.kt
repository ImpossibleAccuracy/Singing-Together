package com.singing.api.domain.repository

import com.singing.api.domain.model.PublicationTag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface PublicationTagRepository : JpaRepository<PublicationTag, Int>, JpaSpecificationExecutor<PublicationTag>