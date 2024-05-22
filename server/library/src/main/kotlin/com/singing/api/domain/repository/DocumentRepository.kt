package com.singing.api.domain.repository

import com.singing.api.domain.model.DocumentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.util.*

interface DocumentRepository : JpaRepository<DocumentEntity, Int>, JpaSpecificationExecutor<DocumentEntity> {

    fun findByPath(path: String): Optional<DocumentEntity>


    fun findByHash(hash: String): Optional<DocumentEntity>


    fun findByVoiceRecordRecords_Id(id: Int): Optional<DocumentEntity>


    fun findByTrackRecords_Id(id: Int): Optional<DocumentEntity>
}