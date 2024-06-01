package com.singing.api.domain.repository

import com.singing.api.domain.model.RecordItemEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface RecordItemRepository : JpaRepository<RecordItemEntity, Int>, JpaSpecificationExecutor<RecordItemEntity> {
    fun findByRecord_IdOrderByTimeAsc(
        id: Int,
        pageable: Pageable,
    ): List<RecordItemEntity>
}