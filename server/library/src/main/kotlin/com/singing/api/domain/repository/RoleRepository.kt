package com.singing.api.domain.repository

import com.singing.api.domain.model.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface RoleRepository : JpaRepository<Role, Int>, JpaSpecificationExecutor<Role>