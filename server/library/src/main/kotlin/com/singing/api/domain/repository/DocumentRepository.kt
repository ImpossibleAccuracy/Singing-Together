package com.singing.api.domain.repository

import com.singing.api.domain.model.DocumentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface DocumentRepository : JpaRepository<DocumentEntity, Int>, JpaSpecificationExecutor<DocumentEntity>