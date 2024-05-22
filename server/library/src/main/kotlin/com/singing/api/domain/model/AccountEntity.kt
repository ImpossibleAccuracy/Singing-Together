package com.singing.api.domain.model

import com.singing.api.domain.model.base.BaseEntity
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "account")
class AccountEntity(
    id: Int? = null,

    @Column(name = "created_at", nullable = false)
    var createdAt: Instant = Instant.now(),

    @Column(name = "username", nullable = false)
    var username: String? = null,

    @Column(name = "password", nullable = false)
    var password: String? = null,

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = DocumentEntity::class)
    @JoinColumn(name = "avatar_id")
    var avatar: DocumentEntity? = null,

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = RoleEntity::class) @JoinTable(
        name = "role_account",
        joinColumns = [JoinColumn(name = "account_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    ) var roles: Set<RoleEntity> = setOf()
) : BaseEntity(id) {
    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, targetEntity = RecordEntity::class)
    var records: Set<RecordEntity> = setOf()

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, targetEntity = PublicationEntity::class)
    var publications: Set<PublicationEntity> = setOf()


    override fun toString(): String {
        return "Account(createdAt=$createdAt, username=$username)"
    }
}
