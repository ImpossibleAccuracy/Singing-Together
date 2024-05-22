package com.singing.api.domain.model

import com.singing.api.domain.model.base.BaseTitleEntity
import jakarta.persistence.*

@Entity
@Table(name = "role")
class RoleEntity(
    id: Int? = null,
    title: String? = null,

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = PrivilegeEntity::class)
    @JoinTable(
        name = "privilege_role",
        joinColumns = [JoinColumn(name = "role_id")],
        inverseJoinColumns = [JoinColumn(name = "privilege_id")]
    )
    var privileges: Set<PrivilegeEntity> = setOf()
) : BaseTitleEntity(id, title) {
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY, targetEntity = AccountEntity::class)
    var accounts: Set<AccountEntity> = setOf()
}
