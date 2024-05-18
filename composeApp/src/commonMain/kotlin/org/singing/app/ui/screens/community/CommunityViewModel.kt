package org.singing.app.ui.screens.community

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch
import org.singing.app.domain.model.PublicationSort
import org.singing.app.domain.repository.publication.PublicationRepository
import org.singing.app.domain.store.account.UserContainer
import org.singing.app.ui.base.AppViewModel
import org.singing.app.ui.screens.community.model.CommunityUiState
import org.singing.app.ui.screens.community.model.PublicationsSearchFilters

@Stable
class CommunityViewModel(
    private val publicationRepository: PublicationRepository,
) : AppViewModel() {
    private val _uiState = MutableStateFlow(CommunityUiState())
    val uiState = _uiState.asStateFlow()

    private var filterAwaitJob: Job? = null
    private var publicationLoadJob: Job? = null

    init {
        viewModelScope.launch {
            launch {
                UserContainer.user.collect { user ->
                    _uiState.update {
                        it.copy(user = user)
                    }
                }
            }

            launch {
                _uiState.update {
                    it.copy(
                        isPopularCategoriesLoading = true,
                    )
                }

                val categories = publicationRepository.getPopularCategories()

                _uiState.update {
                    it.copy(
                        popularCategories = categories.toImmutableList(),
                        isPopularCategoriesLoading = false,
                    )
                }
            }

            nextRandomPublication()
            loadNextPublications()
        }
    }

    fun nextRandomPublication() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    randomPublication = null,
                )
            }

            val publication = publicationRepository.getRandomPublication()

            _uiState.update {
                it.copy(
                    randomPublication = publication,
                )
            }
        }
    }

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
            delay(1500)

            if (uiState.value.searchFilters == newState.searchFilters) {
                _uiState.update {
                    it.copy(
                        currentPage = -1,
                        publications = persistentListOf(),
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

            val newPublications = (uiState.value.publications + loadedPublications).toImmutableList()

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
    }
}
