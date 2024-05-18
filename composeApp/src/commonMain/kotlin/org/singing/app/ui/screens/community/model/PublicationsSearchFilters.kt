package org.singing.app.ui.screens.community.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.singing.app.domain.model.PublicationSort

data class PublicationsSearchFilters(
    val currentTagText: String = "",
    val tags: ImmutableList<String> = persistentListOf(),
    val description: String = "",
    val sort: PublicationSort = PublicationSort.DateCreated,
    val showOwnPublications: Boolean = true,
)
