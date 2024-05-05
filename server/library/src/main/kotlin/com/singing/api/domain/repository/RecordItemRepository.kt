package com.singing.api.domain.repository

import com.singing.api.domain.model.RecordItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface RecordItemRepository : JpaRepository<RecordItem, Int>, JpaSpecificationExecutor<RecordItem>