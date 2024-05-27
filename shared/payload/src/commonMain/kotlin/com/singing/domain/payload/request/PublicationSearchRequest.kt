package com.singing.domain.payload.request


class PublicationSearchRequest(
    val page: Int = 0,
    val tags: List<String>? = null,
    val description: String? = null,
    val showOwnPublications: Boolean = true,
    val sort: String = "DateCreated",
)
