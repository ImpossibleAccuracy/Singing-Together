package com.singing.api.domain.repository

import com.singing.api.domain.model.PrivilegeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface PrivilegeRepository : JpaRepository<PrivilegeEntity, Int>, JpaSpecificationExecutor<PrivilegeEntity>