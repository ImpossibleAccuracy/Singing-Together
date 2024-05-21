package com.singing.api.domain.model

import com.singing.api.domain.model.base.BaseEntity
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "record")
class RecordEntity(
    id: Int? = null,

    @Column(name = "created_at", nullable = false)
    var createdAt: Instant = Instant.now(),

    @Column(name = "duration", nullable = false)
    var duration: Long? = null,

    @Column(name = "accuracy", nullable = false, columnDefinition = "decimal(5,2)")
    var accuracy: Double? = null,

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = AccountEntity::class)
    @JoinColumn(name = "account_id", nullable = false)
    var account: AccountEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = DocumentEntity::class)
    @JoinColumn(name = "voice_record_id", nullable = false)
    var voiceRecord: DocumentEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = DocumentEntity::class)
    @JoinColumn(name = "track_id")
    var track: DocumentEntity? = null,
) : BaseEntity(id) {
    @OneToMany(mappedBy = "record", fetch = FetchType.LAZY, targetEntity = RecordItemEntity::class)
    var points: Set<RecordItemEntity> = setOf()

    @OneToMany(mappedBy = "record", fetch = FetchType.LAZY, targetEntity = PublicationEntity::class)
    var publications: Set<PublicationEntity> = setOf()

    override fun toString(): String {
        return "Record(createdAt=$createdAt, duration=$duration, account=${account?.id})"
    }
}
