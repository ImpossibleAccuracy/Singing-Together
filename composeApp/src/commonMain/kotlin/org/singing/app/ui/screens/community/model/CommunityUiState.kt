package org.singing.app.ui.screens.community.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import org.singing.app.domain.model.AccountUiData
import com.singing.app.domain.model.CategoryInfo
import org.singing.app.domain.model.Publication

data class CommunityUiState(
    val user: AccountUiData? = null,
    val popularCategories: ImmutableList<CategoryInfo> = persistentListOf(),
    val isPopularCategoriesLoading: Boolean = true,
    val randomPublication: Publication? = null,

    val searchFilters: PublicationsSearchFilters = PublicationsSearchFilters(),
    val currentPage: Int = -1,
    val isPublicationsLoading: Boolean = true,
    val canLoadMorePublications: Boolean = true,
    val publications: PersistentList<Publication> = persistentListOf(),
) {
    val isRandomPublicationLoading: Boolean
        get() = randomPublication == null

    val isUserAuthorized: Boolean
        get() = user != null
}
