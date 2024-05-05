package com.singing.api.domain.model

import com.singing.api.domain.model.base.BaseEntity
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "document")
class Document(
    id: Int? = null,

    @Column(name = "created_at", nullable = false)
    var createdAt: Instant = Instant.now(),

    @Column(name = "title", nullable = false)
    var title: String? = null,

    @Column(name = "hash", nullable = false)
    var hash: String? = null,

    @Column(name = "path", nullable = false)
    var path: String? = null,

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = DocumentType::class)
    @JoinColumn(name = "type_id", nullable = false)
    var type: DocumentType? = null,
) : BaseEntity(id) {
    @OneToMany(mappedBy = "avatar", fetch = FetchType.LAZY, targetEntity = Account::class)
    var avatarAccounts: Set<Account> = setOf()

    @OneToMany(mappedBy = "voiceRecord", fetch = FetchType.LAZY, targetEntity = Record::class)
    var voiceRecordRecords: Set<Record> = setOf()

    @OneToMany(mappedBy = "track", fetch = FetchType.LAZY, targetEntity = Record::class)
    var trackRecords: Set<Record> = setOf()


    override fun toString(): String {
        return "Document(createdAt=$createdAt, path=$path, hash=$hash, title=$title)"
    }
}
