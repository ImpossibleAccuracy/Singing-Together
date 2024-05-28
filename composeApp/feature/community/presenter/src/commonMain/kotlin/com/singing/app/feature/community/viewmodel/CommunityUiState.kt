package com.singing.app.feature.community.viewmodel

import com.singing.app.domain.model.DataState
import com.singing.app.domain.model.Publication
import com.singing.app.domain.model.PublicationTagStatistics
import com.singing.app.domain.model.UserData
import com.singing.app.feature.community.model.PublicationsSearchFilters
import kotlinx.collections.immutable.PersistentList

data class CommunityUiState(
    val user: UserData? = null,
    val popularPublicationTags: DataState<PersistentList<PublicationTagStatistics>> = DataState.Loading,
    val randomPublication: DataState<Publication> = DataState.Loading,

    val searchFilters: PublicationsSearchFilters = PublicationsSearchFilters(),
    val isSearchResultsInit: Boolean = false,
) {
    val isUserAuthorized: Boolean
        get() = user != null
}
