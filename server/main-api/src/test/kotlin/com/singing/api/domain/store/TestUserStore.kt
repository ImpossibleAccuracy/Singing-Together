package com.singing.api.domain.store

import com.singing.api.domain.model.AccountEntity
import com.singing.api.domain.model.TestUser
import java.time.Instant

object TestUserStore {
    val AUTH_TOKEN_ADMIN = TestUser(
        token = "321123",
        account = AccountEntity(
            id = 1,
            createdAt = Instant.now(),
            username = "Admin",
            roles = setOf(TestPermissionsStore.ADMIN, TestPermissionsStore.USER)
        )
    )
    val AUTH_TOKEN_USER1 = TestUser(
        token = "qweewqqwe",
        account = AccountEntity(
            id = 2,
            createdAt = Instant.now(),
            username = "TestUser",
            roles = setOf(TestPermissionsStore.USER)
        )
    )
    val AUTH_TOKEN_USER2 = TestUser(
        token = "zxccxzcxzcz",
        account = AccountEntity(
            id = 5,
            createdAt = Instant.now(),
            username = "zxccxzcxzcz",
            roles = setOf(TestPermissionsStore.USER)
        )
    )

    val users = listOf(
        AUTH_TOKEN_USER1,
        AUTH_TOKEN_USER2,
        AUTH_TOKEN_ADMIN,
    )
}
