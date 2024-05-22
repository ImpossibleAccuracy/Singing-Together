package com.singing.api.domain.repository

import com.singing.api.domain.model.RecordEntity
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface RecordRepository : JpaRepository<RecordEntity, Int>, JpaSpecificationExecutor<RecordEntity> {
    fun findByAuthor_Id(id: Int, sort: Sort): List<RecordEntity>
}