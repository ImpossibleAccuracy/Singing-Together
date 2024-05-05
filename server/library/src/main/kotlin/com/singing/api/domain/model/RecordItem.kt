package com.singing.api.domain.model

import com.singing.api.domain.model.base.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "record_item")
class RecordItem(
    id: Int? = null,

    @Column(name = "time", nullable = false)
    var time: Long? = null,

    @Column(name = "frequency", nullable = false)
    var frequency: Int? = null,

    @Column(name = "track_frequency")
    var trackFrequency: Int? = null,

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Record::class)
    @JoinColumn(name = "record_id", nullable = false)
    var record: Record? = null,
) : BaseEntity(id) {
    override fun toString(): String {
        return "RecordItem(time: $time, frequency: $frequency -> $trackFrequency)"
    }
}
