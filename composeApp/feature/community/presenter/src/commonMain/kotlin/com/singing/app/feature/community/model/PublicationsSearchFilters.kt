package com.singing.app.feature.community.model

import com.singing.domain.model.PublicationSort
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class PublicationsSearchFilters(
    val currentTagText: String = "",
    val tags: ImmutableList<String> = persistentListOf(),
    val description: String = "",
    val sort: PublicationSort = PublicationSort.DateCreated,
    val showOwnPublications: Boolean = true,
)
