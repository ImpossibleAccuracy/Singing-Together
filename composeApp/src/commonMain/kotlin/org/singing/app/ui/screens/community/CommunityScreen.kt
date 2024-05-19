package org.singing.app.ui.screens.community

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.dokar.chiptextfield.Chip
import com.dokar.chiptextfield.ChipTextFieldState
import kotlinx.collections.immutable.toImmutableList
import org.singing.app.di.module.viewModels
import org.singing.app.setup.collectAsStateSafe
import org.singing.app.ui.base.connectVerticalNestedScroll
import org.singing.app.ui.common.ContentContainer
import org.singing.app.ui.common.DefaultPagePaddings
import org.singing.app.ui.common.player.RecordPlayer
import org.singing.app.ui.common.player.rememberRecordPlayer
import org.singing.app.ui.screens.account.profile.AccountProfileScreen
import org.singing.app.ui.screens.community.views.*
import org.singing.app.ui.screens.publication.details.PublicationDetailsScreen
import org.singing.app.ui.screens.record.start.SelectRecordTypeScreen


private val sectionShape
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.shapes.small


class CommunityScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel = viewModels<CommunityViewModel>()
        val player = rememberRecordPlayer()

        val verticalScroll = rememberScrollState()

        ContentContainer {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(
                        state = verticalScroll,
                    )
                    .padding(DefaultPagePaddings),
                verticalArrangement = Arrangement.spacedBy(36.dp),
            ) {
                WelcomeViewContainer(
                    viewModel = viewModel,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    Box(
                        modifier = Modifier.weight(5f)
                    ) {
                        PopularCategoriesContainer(
                            modifier = Modifier.fillMaxWidth(),
                            listModifier = Modifier.connectVerticalNestedScroll(1000.dp, verticalScroll),
                            viewModel = viewModel,
                        )
                    }

                    Box(
                        modifier = Modifier.weight(4f)
                    ) {
                        RandomPublicationContainer(
                            modifier = Modifier.fillMaxWidth(),
                            viewModel = viewModel,
                            player = player,
                        )
                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    PublicationSearchFiltersContainer(
                        modifier = Modifier.fillMaxWidth(),
                        viewModel = viewModel,
                    )

                    PublicationSearchResultContainer(
                        modifier = Modifier.fillMaxWidth(),
                        gridModifier = Modifier.connectVerticalNestedScroll(10000.dp, verticalScroll),
                        viewModel = viewModel,
                        player = player,
                    )
                }
            }
        }
    }

    @Composable
    private fun WelcomeViewContainer(
        viewModel: CommunityViewModel,
    ) {
        val navigator = LocalNavigator.currentOrThrow

        WelcomeView(
            modifier = Modifier.fillMaxWidth(),
            onActionClick = {
                if (viewModel.uiState.value.isUserAuthorized) {
                    navigator.push(
                        SelectRecordTypeScreen()
                    )
                } else {
                    // TODO: navigate to auth
                }
            }
        )
    }

    @Composable
    private fun PopularCategoriesContainer(
        modifier: Modifier = Modifier,
        listModifier: Modifier = Modifier,
        viewModel: CommunityViewModel,
    ) {
        val uiState by viewModel.uiState.collectAsStateSafe()

        val popularCategories = uiState.popularCategories
        val isPopularCategoriesLoading = uiState.isPopularCategoriesLoading

        PopularCategories(
            modifier = modifier,
            listModifier = listModifier,
            shape = sectionShape,
            categories = popularCategories,
            isCategoriesLoading = isPopularCategoriesLoading,
        )
    }

    @Composable
    private fun RandomPublicationContainer(
        modifier: Modifier = Modifier,
        viewModel: CommunityViewModel,
        player: RecordPlayer,
    ) {
        val navigator = LocalNavigator.currentOrThrow

        val uiState by viewModel.uiState.collectAsStateSafe()

        LaunchedEffect(uiState.randomPublication) {
            player.reset()
        }

        RandomPublication(
            modifier = modifier,
            shape = sectionShape,
            player = player,
            isLoading = uiState.isRandomPublicationLoading || uiState.randomPublication == null,
            publication = uiState.randomPublication,
            onReload = viewModel::nextRandomPublication,
            onAuthorClick = {
                navigator.push(
                    AccountProfileScreen(
                        requestedAccount = it.author,
                    )
                )
            },
            navigatePublicationDetails = {
                navigator.push(
                    PublicationDetailsScreen(
                        requestedPublication = it,
                    )
                )
            },
        )
    }

    @Composable
    private fun PublicationSearchFiltersContainer(
        modifier: Modifier = Modifier,
        viewModel: CommunityViewModel,
    ) {
        val uiState by viewModel.uiState.collectAsStateSafe()

        val filters = uiState.searchFilters

        val tags by remember {
            derivedStateOf {
                ChipTextFieldState(filters.tags.map(::Chip))
            }
        }

        PublicationSearchFilters(
            modifier = modifier,
            data = PublicationSearchData(
                currentTagText = filters.currentTagText,
                tags = tags,
                showUserPublications = filters.showOwnPublications,
                sortType = filters.sort,
                description = filters.description,
            ),
            actions = PublicationSearchActions(
                onTagTextUpdated = {
                    viewModel.updateSearchFilters(
                        currentTagText = it
                    )
                },
                onTagSubmit = {
                    viewModel.updateSearchFilters(
                        tags = filters.tags
                            .plus(it)
                            .toImmutableList()
                    )

                    Chip(it)
                },
                onTagRemove = { chip ->
                    viewModel.updateSearchFilters(
                        tags = filters.tags
                            .filter { it != chip.text }
                            .toImmutableList()
                    )

                    tags.removeChip(chip)
                },
                onShowUserPublicationsChanged = {
                    viewModel.updateSearchFilters(
                        showOwnPublications = it
                    )
                },
                onSortTypeChanged = {
                    viewModel.updateSearchFilters(
                        sort = it
                    )
                },
                onDescriptionUpdated = {
                    viewModel.updateSearchFilters(
                        description = it
                    )
                }
            )
        )
    }

    @Composable
    private fun PublicationSearchResultContainer(
        modifier: Modifier = Modifier,
        gridModifier: Modifier = Modifier,
        viewModel: CommunityViewModel,
        player: RecordPlayer,
    ) {
        val navigator = LocalNavigator.currentOrThrow
        val uiState by viewModel.uiState.collectAsStateSafe()

        PublicationSearchResult(
            modifier = modifier,
            gridModifier = gridModifier,
            publications = uiState.publications,
            player = player,
            isLoaderVisible = uiState.canLoadMorePublications,
            navigatePublicationDetails = {
                navigator.push(
                    PublicationDetailsScreen(
                        requestedPublication = it,
                    )
                )
            },
            onLoadMorePublications = {
                viewModel.loadNextPublications()
            }
        )
    }
}
