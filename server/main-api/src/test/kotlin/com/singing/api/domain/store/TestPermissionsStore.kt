package com.singing.api.domain.store

import com.singing.api.domain.model.RoleEntity

object TestPermissionsStore {
    val ADMIN = RoleEntity(
        id = 1,
        title = "Admin",
        privileges = setOf()
    )

    val USER = RoleEntity(
        id = 2,
        title = "User",
        privileges = setOf()
    )
}
