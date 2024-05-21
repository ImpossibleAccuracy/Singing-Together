package com.singing.api.domain.model

import com.singing.api.domain.model.base.BaseEntity
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "document")
class DocumentEntity(
    id: Int? = null,

    @Column(name = "created_at", nullable = false)
    var createdAt: Instant = Instant.now(),

    @Column(name = "title", nullable = false)
    var title: String? = null,

    @Column(name = "hash", nullable = false)
    var hash: String? = null,

    @Column(name = "path", nullable = false)
    var path: String? = null,

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = DocumentTypeEntity::class)
    @JoinColumn(name = "type_id", nullable = false)
    var type: DocumentTypeEntity? = null,
) : BaseEntity(id) {
    @OneToMany(mappedBy = "avatar", fetch = FetchType.LAZY, targetEntity = AccountEntity::class)
    var avatarAccounts: Set<AccountEntity> = setOf()

    @OneToMany(mappedBy = "voiceRecord", fetch = FetchType.LAZY, targetEntity = RecordEntity::class)
    var voiceRecordRecords: Set<RecordEntity> = setOf()

    @OneToMany(mappedBy = "track", fetch = FetchType.LAZY, targetEntity = RecordEntity::class)
    var trackRecords: Set<RecordEntity> = setOf()


    override fun toString(): String {
        return "Document(createdAt=$createdAt, path=$path, hash=$hash, title=$title)"
    }
}
