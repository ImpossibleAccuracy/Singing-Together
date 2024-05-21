package com.singing.api.domain.model

import com.singing.api.domain.model.base.BaseEntity
import jakarta.persistence.*
import jakarta.validation.constraints.Digits

@Entity
@Table(name = "record_item")
class RecordItemEntity(
    id: Int? = null,

    @Column(name = "time", nullable = false)
    var time: Long? = null,

    @Column(name = "frequency", nullable = false, columnDefinition = "decimal(7, 2)")
    var frequency: Double? = null,

    @Column(name = "track_frequency", columnDefinition = "decimal(7, 2)")
    var trackFrequency: Double? = null,

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = RecordEntity::class)
    @JoinColumn(name = "record_id", nullable = false)
    var record: RecordEntity? = null,
) : BaseEntity(id) {
    override fun toString(): String {
        return "RecordItem(time: $time, frequency: $frequency -> $trackFrequency)"
    }
}
