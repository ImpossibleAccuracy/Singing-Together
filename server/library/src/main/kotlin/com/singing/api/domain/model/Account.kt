package com.singing.api.domain.model

import com.singing.api.domain.model.base.BaseEntity
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "account")
class Account(
    id: Int? = null,

    @Column(name = "created_at", nullable = false)
    var createdAt: Instant = Instant.now(),

    @Column(name = "username", nullable = false)
    var username: String? = null,

    @Column(name = "password", nullable = false)
    var password: String? = null,

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Document::class)
    @JoinColumn(name = "avatar_id")
    var avatar: Document? = null
) : BaseEntity(id) {
    @ManyToMany(fetch = FetchType.EAGER, targetEntity = Role::class)
    @JoinTable(
        name = "role_account",
        joinColumns = [JoinColumn(name = "account_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    var roles: Set<Role> = setOf()

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, targetEntity = Record::class)
    var records: Set<Record> = setOf()

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, targetEntity = Publication::class)
    var publications: Set<Publication> = setOf()


    override fun toString(): String {
        return "Account(createdAt=$createdAt, username=$username)"
    }
}
