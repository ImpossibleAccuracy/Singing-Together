package com.singing.app.domain.payload

import com.singing.app.domain.model.PublicationSort

class PublicationSearchRequest(
    val page: Int = 0,
    val tags: List<String>? = null,
    val description: String? = null,
    val showOwnPublications: Boolean = true,
    val sort: PublicationSort = PublicationSort.DateCreated,
)
