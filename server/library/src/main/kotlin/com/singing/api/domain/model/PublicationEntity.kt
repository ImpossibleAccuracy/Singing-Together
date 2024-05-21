package com.singing.api.domain.model

import com.singing.api.domain.model.base.BaseEntity
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "publication")
class PublicationEntity(
    id: Int? = null,

    @Column(name = "created_at", nullable = false)
    var createdAt: Instant = Instant.now(),

    @Column(name = "description", nullable = false, columnDefinition = "text")
    var description: String? = null,

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = AccountEntity::class)
    @JoinColumn(name = "account_id", nullable = false)
    var account: AccountEntity? = null,

    @OneToOne(fetch = FetchType.LAZY, targetEntity = RecordEntity::class)
    @JoinColumn(name = "record_id", nullable = false)
    var record: RecordEntity? = null,
) : BaseEntity(id) {
    @ManyToMany(fetch = FetchType.LAZY, targetEntity = PublicationTagEntity::class)
    @JoinTable(
        name = "publication_tags",
        joinColumns = [JoinColumn(name = "publication_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")]
    )
    var tags: Set<PublicationTagEntity> = setOf()


    override fun toString(): String {
        return "Publication(createdAt=$createdAt, description=$description, record=${record?.id})"
    }
}
