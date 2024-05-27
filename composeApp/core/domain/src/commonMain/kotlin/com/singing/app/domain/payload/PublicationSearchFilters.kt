package com.singing.app.domain.payload

import com.singing.domain.model.PublicationSort

data class PublicationSearchFilters(
    val sort: PublicationSort,
    val tags: List<String>?,
    val description: String?,
    val showOwnPublications: Boolean,
)
