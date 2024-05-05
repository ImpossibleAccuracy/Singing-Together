package com.singing.api.domain.model

import com.singing.api.domain.model.base.BaseEntity
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "publication")
class Publication(
    id: Int? = null,

    @Column(name = "created_at", nullable = false)
    var createdAt: Instant = Instant.now(),

    @Column(name = "description", nullable = false, columnDefinition = "text")
    var description: String? = null,

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Account::class)
    @JoinColumn(name = "account_id", nullable = false)
    var account: Account? = null,

    @OneToOne(fetch = FetchType.LAZY, targetEntity = Record::class)
    @JoinColumn(name = "record_id", nullable = false)
    var record: Record? = null,
) : BaseEntity(id) {
    @ManyToMany(fetch = FetchType.LAZY, targetEntity = PublicationTag::class)
    @JoinTable(
        name = "publication_tags",
        joinColumns = [JoinColumn(name = "publication_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")]
    )
    var tags: Set<PublicationTag> = setOf()


    override fun toString(): String {
        return "Publication(createdAt=$createdAt, description=$description, record=${record?.id})"
    }
}
