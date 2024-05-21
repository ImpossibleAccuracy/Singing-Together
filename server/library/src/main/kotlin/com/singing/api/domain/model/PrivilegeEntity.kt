package com.singing.api.domain.model

import com.singing.api.domain.model.base.BaseTitleEntity
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ManyToMany
import jakarta.persistence.Table

@Entity
@Table(name = "privilege")
class PrivilegeEntity(
    id: Int? = null,
) : BaseTitleEntity(id) {
    @ManyToMany(mappedBy = "privileges", fetch = FetchType.LAZY, targetEntity = RoleEntity::class)
    var roles: Set<RoleEntity> = setOf()
}
