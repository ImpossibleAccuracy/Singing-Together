package com.singing.api.service.publication

import com.singing.api.domain.model.PublicationEntity
import com.singing.api.domain.model.RecordEntity
import com.singing.domain.model.PublicationSort
import org.springframework.data.domain.Sort

fun PublicationSort.toOrder() = when (this) {
    PublicationSort.DateCreated -> Sort.Order(
        Sort.Direction.DESC,
        PublicationEntity::createdAt.name
    )

    PublicationSort.Accuracy -> Sort.Order(
        Sort.Direction.DESC,
        "${PublicationEntity::record.name}.${RecordEntity::accuracy.name}"
    )

    PublicationSort.Duration -> Sort.Order(
        Sort.Direction.DESC,
        "${PublicationEntity::record.name}.${RecordEntity::duration.name}"
    )
}
