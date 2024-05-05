package com.singing.api.domain.repository

import com.singing.api.domain.model.Publication
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface PublicationRepository : JpaRepository<Publication, Int>, JpaSpecificationExecutor<Publication> {

    fun findByAccount_Id(id: Int): List<Publication>
}
