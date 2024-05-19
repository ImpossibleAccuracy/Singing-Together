package org.singing.app.ui.screens.community

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.dokar.chiptextfield.Chip
import com.dokar.chiptextfield.ChipTextFieldState
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.baseline_shuffle_variant_24
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.di.module.viewModels
import org.singing.app.setup.collectAsStateSafe
import org.singing.app.ui.base.Space
import org.singing.app.ui.base.connectVerticalNestedScroll
import org.singing.app.ui.common.ContentContainer
import org.singing.app.ui.common.player.RecordPlayer
import org.singing.app.ui.common.player.RecordPlayerScreen
import org.singing.app.ui.common.player.rememberRecordPlayer
import org.singing.app.ui.screens.account.profile.AccountProfileScreen
import org.singing.app.ui.screens.community.views.*
import org.singing.app.ui.screens.publication.details.PublicationDetailsScreen
import org.singing.app.ui.screens.record.audio.SelectRecordTypeScreen
import org.singing.app.ui.views.base.publication.PublicationCardWithPlayer
import org.singing.app.ui.views.base.publication.publicationCardAppearance


private val sectionShape
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.shapes.small


class CommunityScreen : RecordPlayerScreen() {
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
                    .padding(
                        top = 16.dp,
                        bottom = 24.dp,
                    ),
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

        Column(
            modifier = publicationCardAppearance(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                shape = sectionShape,
                padding = PaddingValues(
                    start = 16.dp,
                    top = 4.dp,
                    end = 16.dp,
                    bottom = 16.dp,
                ),
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Random latest publication",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                )

                Spacer(Modifier.weight(1f))

                IconButton(
                    onClick = viewModel::nextRandomPublication,
                ) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.baseline_shuffle_variant_24),
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = "",
                    )
                }
            }

            Space(4.dp)

            if (uiState.isRandomPublicationLoading || uiState.randomPublication == null) {
                Box(
                    modifier = modifier,
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            } else {
                val publication = uiState.randomPublication!!

                PublicationCardWithPlayer(
                    modifier = modifier,
                    publication = publication,
                    player = player,
                    onAuthorClick = {
                        navigator.push(
                            AccountProfileScreen(
                                requestedAccount = publication.author,
                            )
                        )
                    },
                    navigatePublicationDetails = {
                        navigator.push(
                            PublicationDetailsScreen(
                                requestedPublication = publication,
                            )
                        )
                    },
                )
            }
        }
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
