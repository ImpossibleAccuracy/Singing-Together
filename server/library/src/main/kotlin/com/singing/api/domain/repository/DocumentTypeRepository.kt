package com.singing.api.domain.repository

import com.singing.api.domain.model.DocumentTypeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface DocumentTypeRepository : JpaRepository<DocumentTypeEntity, Int>,
    JpaSpecificationExecutor<DocumentTypeEntity> {

    fun findByMimeType(mimeType: String): Optional<DocumentTypeEntity>
}