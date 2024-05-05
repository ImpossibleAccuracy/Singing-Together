package com.singing.api.domain.model

import com.singing.api.domain.model.base.BaseEntity
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "record")
class Record(
    id: Int? = null,

    @Column(name = "created_at", nullable = false)
    var createdAt: Instant = Instant.now(),

    @Column(name = "duration", nullable = false)
    var duration: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Account::class)
    @JoinColumn(name = "account_id", nullable = false)
    var account: Account? = null,

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Document::class)
    @JoinColumn(name = "voice_record_id", nullable = false)
    var voiceRecord: Document? = null,

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Document::class)
    @JoinColumn(name = "track_id")
    var track: Document? = null,
) : BaseEntity(id) {
    @OneToMany(mappedBy = "record", fetch = FetchType.LAZY, targetEntity = RecordItem::class)
    var points: Set<RecordItem> = setOf()

    @OneToMany(mappedBy = "record", fetch = FetchType.LAZY, targetEntity = Publication::class)
    var publications: Set<Publication> = setOf()
}
