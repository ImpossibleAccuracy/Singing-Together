package com.singing.app.feature.community.viewmodel

import com.singing.app.feature.community.model.PublicationsSearchFilters

sealed interface CommunityIntent {
    data object Search : CommunityIntent

    data class UpdateSearchFilters(
        val filters: PublicationsSearchFilters
    ) : CommunityIntent

    data object ReloadRandomPublication : CommunityIntent
}