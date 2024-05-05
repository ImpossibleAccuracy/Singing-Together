package com.singing.api.domain.repository

import com.singing.api.domain.model.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface AccountRepository : JpaRepository<Account, Int>, JpaSpecificationExecutor<Account> {
    fun findByUsernameIgnoreCase(username: String): Account?
}
