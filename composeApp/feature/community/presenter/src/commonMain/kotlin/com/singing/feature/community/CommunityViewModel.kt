package com.singing.feature.community

import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.singing.app.domain.model.Publication
import com.singing.app.domain.payload.PublicationSearchFilters
import com.singing.app.domain.provider.UserProvider
import com.singing.feature.community.viewmodel.CommunityIntent
import com.singing.feature.community.viewmodel.CommunityUiState
import com.singing.feature.community.domain.usecase.GetPopularPublicationTagsUseCase
import com.singing.feature.community.domain.usecase.GetRandomPublicationUseCase
import com.singing.feature.community.domain.usecase.SearchPublicationsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Stable
class CommunityViewModel(
    private val userProvider: UserProvider,
    private val getPopularPublicationTagsUseCase: GetPopularPublicationTagsUseCase,
    private val getRandomPublicationUseCase: GetRandomPublicationUseCase,
    private val searchPublicationsUseCase: SearchPublicationsUseCase,
) : ScreenModel {
    private val _uiState = MutableStateFlow(CommunityUiState())
    val uiState = _uiState.asStateFlow()

    private val _searchResults = MutableStateFlow<PagingData<Publication>>(PagingData.empty())
    val searchResults = _searchResults.asStateFlow()

    init {
        screenModelScope.launch {
            nextRandomPublication()
        }

        screenModelScope.launch {
            userProvider.userFlow.collect { user ->
                _uiState.update {
                    it.copy(user = user)
                }
            }
        }

        screenModelScope.launch {
            getPopularPublicationTagsUseCase().collect { tags ->
                _uiState.update {
                    it.copy(popularPublicationTags = tags)
                }
            }
        }
    }


    fun onIntent(intent: CommunityIntent) {
        screenModelScope.launch {
            when (intent) {
                CommunityIntent.ReloadRandomPublication -> {
                    nextRandomPublication()
                }

                is CommunityIntent.UpdateSearchFilters -> {
                    _uiState.update {
                        it.copy(
                            searchFilters = intent.filters,
                        )
                    }
                }

                CommunityIntent.Search -> {
                    executeSearch()
                }
            }
        }
    }

    private suspend fun nextRandomPublication() {
        getRandomPublicationUseCase()
            .collect { data ->
                _uiState.update {
                    it.copy(
                        randomPublication = data,
                    )
                }
            }
    }

    private suspend fun executeSearch() {
        _uiState.update { it.copy(isSearchResultsInit = false) }

        searchPublicationsUseCase(filters = computeSearchFilters())
            .onStart {
                _uiState.update { it.copy(isSearchResultsInit = false) }
            }
            .collect {
                _searchResults.value = it
            }
    }

    private fun computeSearchFilters(): PublicationSearchFilters {
        val filters = uiState.value.searchFilters

        val tags = filters.tags
            .plus(filters.currentTagText.trim())
            .filter { it.isNotBlank() }
            .takeIf { it.isNotEmpty() }

        val description = filters.description.takeIf { it.isNotBlank() }

        return PublicationSearchFilters(
            sort = filters.sort,
            tags = tags,
            description = description,
            showOwnPublications = filters.showOwnPublications,
        )
    }

    /*

    fun updateSearchFilters(
        currentTagText: String = uiState.value.searchFilters.currentTagText,
        tags: ImmutableList<String> = uiState.value.searchFilters.tags,
        description: String = uiState.value.searchFilters.description,
        sort: PublicationSort = uiState.value.searchFilters.sort,
        showOwnPublications: Boolean = uiState.value.searchFilters.showOwnPublications,
    ) {
        val newState = _uiState.updateAndGet {
            it.copy(
                searchFilters = PublicationsSearchFilters(
                    currentTagText = currentTagText,
                    tags = tags,
                    description = description,
                    sort = sort,
                    showOwnPublications = showOwnPublications,
                ),
            )
        }

        filterAwaitJob?.cancel()

        filterAwaitJob = viewModelScope.launch {
            if (uiState.value.searchFilters == newState.searchFilters) {
                _uiState.update {
                    it.copy(
                        currentPage = -1,
                        publications = persistentListOf(),
                        canLoadMorePublications = true,
                    )
                }

                loadNextPublications(force = true)
            }
        }
    }

    fun loadNextPublications(
        force: Boolean = false
    ) {
        if (publicationLoadJob?.isActive == true && force) return

        if (!uiState.value.canLoadMorePublications) return

        publicationLoadJob?.cancel()

        publicationLoadJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isPublicationsLoading = true,
                )
            }

            val prevPage = uiState.value.currentPage
            val currentPage = prevPage + 1
            val filters = uiState.value.searchFilters

            val tags = filters.tags
                .plus(filters.currentTagText.trim())
                .filter { it.isNotBlank() }
                .takeIf { it.isNotEmpty() }

            val description = filters.description.takeIf { it.isNotBlank() }

            val loadedPublications = publicationRepository.loadPublicationsByFilters(
                page = currentPage,
                tags = tags,
                description = description,
                showOwnPublications = filters.showOwnPublications,
                sort = filters.sort,
            )

            delay(1000)

            val newPage =
                if (loadedPublications.isEmpty()) prevPage
                else currentPage

            val newPublications = (uiState.value.publications + loadedPublications)
                .toPersistentList()

            _uiState.update {
                it.copy(
                    currentPage = newPage,
                    isPublicationsLoading = false,
                    publications = newPublications,
                    canLoadMorePublications = loadedPublications.isNotEmpty(),
                )
            }

            publicationLoadJob = null
        }
    }*/
}
