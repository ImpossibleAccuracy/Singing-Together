package com.singing.api.domain.repository

import com.singing.api.domain.model.AccountEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface AccountRepository : JpaRepository<AccountEntity, Int>, JpaSpecificationExecutor<AccountEntity> {
    fun findByUsernameIgnoreCase(username: String): AccountEntity?
}
