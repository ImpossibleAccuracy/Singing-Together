package com.singing.api.domain.repository

import com.singing.api.domain.model.Document
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface DocumentRepository : JpaRepository<Document, Int>, JpaSpecificationExecutor<Document>