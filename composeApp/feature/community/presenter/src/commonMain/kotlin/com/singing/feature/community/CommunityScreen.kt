package com.singing.feature.community

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.navigator.currentOrThrow
import com.singing.app.feature.rememberRecordPlayer
import com.singing.app.navigation.AppNavigator
import com.singing.app.navigation.SharedScreen
import com.singing.app.navigation.views.ContentContainer
import com.singing.app.ui.connectVerticalNestedScroll
import com.singing.app.ui.screen.WindowSize
import com.singing.app.ui.screen.actualScreenSize
import com.singing.app.ui.screen.dimens
import com.singing.feature.community.viewmodel.CommunityIntent
import com.singing.feature.community.viewmodel.CommunityUiState
import com.singing.feature.community.views.PopularCategories
import com.singing.feature.community.views.RandomPublication
import com.singing.feature.community.views.WelcomeView
import com.singing.feature.community.views.search.PublicationSearchFiltersContainer
import com.singing.feature.community.views.search.PublicationSearchResultContainer


private val sectionShape
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.shapes.small


@Composable
fun CommunityScreen(
    modifier: Modifier = Modifier,
    viewModel: CommunityViewModel,
    uiState: CommunityUiState,
) {
    val navigator = AppNavigator.currentOrThrow
    val verticalScroll = rememberScrollState()

    ContentContainer {
        Column(
            modifier = Modifier
                .verticalScroll(state = verticalScroll)
                .then(modifier),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen4_5),
        ) {
            WelcomeView(
                modifier = Modifier.fillMaxWidth(),
                onActionClick = {
                    if (uiState.isUserAuthorized) {
                        navigator.navigate(
                            SharedScreen.SelectRecordingType
                        )
                    } else {
                        navigator.navigate(
                            SharedScreen.Auth
                        )
                    }
                }
            )

            Box {
                val publicationTags = remember(uiState.popularPublicationTags) {
                    movableContentWithReceiverOf<Modifier, Modifier> { listModifier ->
                        PopularCategories(
                            modifier = this,
                            listModifier = listModifier.fillMaxWidth(),
                            shape = sectionShape,
                            categories = uiState.popularPublicationTags,
                        )
                    }
                }

                val randomPublication = remember(uiState.randomPublication) {
                    movableContentWithReceiverOf<Modifier> {
                        val player = rememberRecordPlayer()

                        LaunchedEffect(uiState.randomPublication) {
                            player.reset()
                        }

                        RandomPublication(
                            modifier = this,
                            shape = sectionShape,
                            player = player,
                            publication = uiState.randomPublication,
                            onReload = {
                                viewModel.onIntent(CommunityIntent.ReloadRandomPublication)
                            },
                            onAuthorClick = {
                                navigator.navigate(
                                    SharedScreen.UserProfile(it.author)
                                )
                            },
                            navigatePublicationDetails = {
                                navigator.navigate(
                                    SharedScreen.PublicationDetails(it)
                                )
                            },
                        )
                    }
                }

                if (MaterialTheme.actualScreenSize >= WindowSize.EXPANDED) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen3),
                    ) {
                        publicationTags(
                            Modifier.weight(5f),
                            Modifier.connectVerticalNestedScroll(600.dp, verticalScroll)
                        )

                        randomPublication(Modifier.weight(4f))
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen3),
                    ) {
                        publicationTags(
                            Modifier.fillMaxWidth(),
                            Modifier.connectVerticalNestedScroll(1000.dp, verticalScroll)
                        )

                        randomPublication(Modifier.fillMaxWidth())
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen3),
            ) {
                val searchResults = viewModel.searchResults.collectAsLazyPagingItems()

                LaunchedEffect(Unit) {
                    if (!uiState.isSearchResultsInit || searchResults.itemCount == 0) {
                        viewModel.onIntent(CommunityIntent.Search)
                    }
                }

                PublicationSearchFiltersContainer(
                    modifier = Modifier.fillMaxWidth(),
                    searchFilters = uiState.searchFilters,
                    onFiltersUpdate = {
                        viewModel.onIntent(
                            CommunityIntent.UpdateSearchFilters(it, false)
                        )
                    },
                )

                PublicationSearchResultContainer(
                    modifier = Modifier.fillMaxWidth(),
                    gridModifier = Modifier.connectVerticalNestedScroll(
                        100000.dp,
                        verticalScroll
                    ), // FIXME: bad way to display list
                    uiState = uiState,
                    searchResults = searchResults,
                    navigate = {
                        navigator.navigate(it)
                    },
                )
            }
        }
    }
}
