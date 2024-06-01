package com.singing.api.domain.repository

import com.singing.api.domain.model.PublicationEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.time.Instant
import java.util.*

interface PublicationRepository : JpaRepository<PublicationEntity, Int>, JpaSpecificationExecutor<PublicationEntity> {
    fun findByAccount_Id(id: Int, pageable: Pageable): List<PublicationEntity>

    fun findByRecord_Id(id: Int): Optional<PublicationEntity>

    fun findByCreatedAtGreaterThanEqual(createdAt: Instant, pageable: Pageable): List<PublicationEntity>

    fun countByAccount_Id(id: Int): Long

    fun existsByRecord_Id(id: Int): Boolean


    fun countByCreatedAtGreaterThanEqual(createdAt: Instant): Long
}
