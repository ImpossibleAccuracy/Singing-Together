package com.singing.feature.community.viewmodel

import com.singing.feature.community.model.PublicationsSearchFilters

sealed interface CommunityIntent {
    data object Search : CommunityIntent

    data class UpdateSearchFilters(
        val filters: PublicationsSearchFilters,
        val immediate: Boolean = true,
    ) : CommunityIntent

    data object ReloadRandomPublication : CommunityIntent
}