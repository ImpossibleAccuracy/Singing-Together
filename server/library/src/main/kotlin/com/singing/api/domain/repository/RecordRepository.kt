package com.singing.api.domain.repository

import com.singing.api.domain.model.Record
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface RecordRepository : JpaRepository<Record, Int>, JpaSpecificationExecutor<Record> {
    fun findByAccount_Id(id: Int): List<Record>
}