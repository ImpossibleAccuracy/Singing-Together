package com.singing.feature.community

import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.singing.app.domain.model.Publication
import com.singing.app.domain.payload.PublicationSearchFilters
import com.singing.app.domain.provider.UserProvider
import com.singing.feature.community.domain.usecase.GetPopularPublicationTagsUseCase
import com.singing.feature.community.domain.usecase.GetRandomPublicationUseCase
import com.singing.feature.community.domain.usecase.SearchPublicationsUseCase
import com.singing.feature.community.viewmodel.CommunityIntent
import com.singing.feature.community.viewmodel.CommunityUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val SearchFiltersAwaitDelay = 700L

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

    private var searchFiltersAwaitJob: Job? = null

    init {
        screenModelScope.launch {
            nextRandomPublication()
        }

        screenModelScope.launch {
            combine(
                userProvider.userFlow,
                getPopularPublicationTagsUseCase()
            ) { user, tags ->
                _uiState.update {
                    it.copy(
                        user = user,
                        popularPublicationTags = tags
                    )
                }
            }.collect {}
        }
    }


    fun onIntent(intent: CommunityIntent) {
        when (intent) {
            CommunityIntent.ReloadRandomPublication -> {
                screenModelScope.launch {
                    nextRandomPublication()
                }
            }

            is CommunityIntent.UpdateSearchFilters -> {
                _uiState.update {
                    it.copy(
                        searchFilters = intent.filters,
                    )
                }

                searchFiltersAwaitJob?.cancel()

                searchFiltersAwaitJob = screenModelScope.launch {
                    if (!intent.immediate) {
                        delay(SearchFiltersAwaitDelay)
                    }

                    executeSearch()

                    searchFiltersAwaitJob = null
                }
            }

            CommunityIntent.Search -> {
                screenModelScope.launch {
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
                _uiState.update { it.copy(isSearchResultsInit = true) }
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
            ?.distinct()

        val description = filters.description.takeIf { it.isNotBlank() }

        return PublicationSearchFilters(
            sort = filters.sort,
            tags = tags,
            description = description,
            showOwnPublications = filters.showOwnPublications,
        )
    }
}
