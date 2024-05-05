package com.singing.api.domain.repository

import com.singing.api.domain.model.DocumentType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface DocumentTypeRepository : JpaRepository<DocumentType, Int>, JpaSpecificationExecutor<DocumentType>