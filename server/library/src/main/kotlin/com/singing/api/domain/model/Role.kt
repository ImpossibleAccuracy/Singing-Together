package com.singing.api.domain.model

import com.singing.api.domain.model.base.BaseTitleEntity
import jakarta.persistence.*

@Entity
@Table(name = "role")
class Role(
    id: Int? = null
) : BaseTitleEntity(id) {
    @ManyToMany(fetch = FetchType.EAGER, targetEntity = Privilege::class)
    @JoinTable(
        name = "privilege_role",
        joinColumns = [JoinColumn(name = "role_id")],
        inverseJoinColumns = [JoinColumn(name = "privilege_id")]
    )
    var privileges: Set<Privilege> = setOf()

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY, targetEntity = Account::class)
    var accounts: Set<Account> = setOf()
}
