package com.singing.api.domain.repository

import com.singing.api.domain.model.Privilege
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface PrivilegeRepository : JpaRepository<Privilege, Int>, JpaSpecificationExecutor<Privilege>