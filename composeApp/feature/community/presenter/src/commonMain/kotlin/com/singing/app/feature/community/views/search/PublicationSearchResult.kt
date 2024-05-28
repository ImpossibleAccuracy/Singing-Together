package com.singing.app.feature.community.views.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.itemContentType
import app.cash.paging.compose.itemKey
import com.singing.app.common.views.base.list.EmptyView
import com.singing.app.common.views.base.list.Loader
import com.singing.app.common.views.base.publication.PublicationCard
import com.singing.app.common.views.base.publication.publicationCardAppearance
import com.singing.app.common.views.model.actions.PublicationCardActions
import com.singing.app.common.views.shared.publication.MainPublicationCard
import com.singing.app.common.views.shared.record.dialog.RecordPlayDialog
import com.singing.app.common.views.toPlayerController
import com.singing.app.common.views.toPublicationCardData
import com.singing.app.common.views.toRecordCardData
import com.singing.app.common.views.toUserUiData
import com.singing.app.domain.features.RecordPlayer
import com.singing.app.domain.model.Publication
import com.singing.app.feature.community.viewmodel.CommunityUiState
import com.singing.app.feature.rememberRecordPlayer
import com.singing.app.navigation.SharedScreen
import com.singing.app.ui.screen.dimens
import com.singing.feature.community.presenter.generated.resources.Res
import com.singing.feature.community.presenter.generated.resources.subtitle_no_search_result
import com.singing.feature.community.presenter.generated.resources.title_no_search_result
import org.jetbrains.compose.resources.stringResource


@Composable
fun PublicationSearchResultContainer(
    modifier: Modifier = Modifier,
    uiState: CommunityUiState,
    searchResults: LazyPagingItems<Publication>,
    navigate: (SharedScreen) -> Unit,
) {
    val mainRecordPlayer = rememberRecordPlayer()

    var publicationToPlay by remember { mutableStateOf<Publication?>(null) }
    if (publicationToPlay != null) {
        val dialogRecordPlayer = rememberRecordPlayer()

        RecordPlayDialog(
            playerController = dialogRecordPlayer.toPlayerController(publicationToPlay!!.record),
            record = publicationToPlay!!.record.toRecordCardData(),
            creator = publicationToPlay!!.author.toUserUiData(),
            onDismiss = {
                publicationToPlay = null
            }
        )
    }

    if (uiState.isSearchResultsInit) {
        PublicationSearchResult(
            modifier = modifier,
            searchResults = searchResults,
            player = mainRecordPlayer,
            playPublication = {
                publicationToPlay = it
            },
            onAuthorClick = {
                navigate(
                    SharedScreen.UserProfile(it.author)
                )
            },
            navigatePublicationDetails = {
                navigate(
                    SharedScreen.PublicationDetails(it)
                )
            },
        )
    } else {
        // TODO
        Text("Search results will be here")
    }
}


@Composable
fun PublicationSearchResult(
    modifier: Modifier = Modifier,
    gridModifier: Modifier = Modifier,
    player: RecordPlayer,
    searchResults: LazyPagingItems<Publication>,
    playPublication: (Publication) -> Unit,
    onAuthorClick: (Publication) -> Unit,
    navigatePublicationDetails: (Publication) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1_5),
    ) {
        SearchResults(
            contentPadding = PaddingValues(),
            searchResults = searchResults
        ) { padding, loader ->
            LazyVerticalStaggeredGrid(
                modifier = gridModifier,
                contentPadding = padding,
                columns = StaggeredGridCells.Adaptive(minSize = 360.dp),
                verticalItemSpacing = MaterialTheme.dimens.dimen1_5,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1_5)
            ) {
                if (searchResults.itemCount > 0) {
                    item(
                        span = StaggeredGridItemSpan.FullLine,
                        contentType = "PrimaryResult"
                    ) {
                        val mainPublication = searchResults[0]

                        if (mainPublication != null) {
                            MainPublicationCard(
                                playerController = player.toPlayerController(mainPublication.record),
                                recordUiData = mainPublication.record.toRecordCardData(),
                                publicationData = mainPublication.toPublicationCardData(false),
                                actions = PublicationCardActions(
                                    onAuthorClick = {
                                        onAuthorClick(mainPublication)
                                    },
                                    navigatePublicationDetails = {
                                        navigatePublicationDetails(mainPublication)
                                    },
                                ),
                            )
                        }
                    }
                }

                if (searchResults.itemCount > 1) {
                    items(
                        count = searchResults.itemCount - 1,
                        key = searchResults.itemKey(),
                        contentType = searchResults.itemContentType()
                    ) { index ->
                        val item = searchResults[index + 1]

                        if (item != null) {
                            PublicationCard(
                                modifier = publicationCardAppearance(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                                ),
                                data = item.toPublicationCardData(false),
                                actions = PublicationCardActions(
                                    onPlay = {
                                        playPublication(item)
                                    },
                                    onAuthorClick = {
                                        onAuthorClick(item)
                                    },
                                    navigatePublicationDetails = {
                                        navigatePublicationDetails(item)
                                    }
                                ),
                            )
                        }
                    }
                }

                item { loader() }
            }
        }
    }
}

@Composable
private fun SearchResults(
    contentPadding: PaddingValues,
    searchResults: LazyPagingItems<Publication>,
    content: @Composable (PaddingValues, @Composable () -> Unit) -> Unit,
) {
    with(searchResults.loadState) {
        when {
            refresh is LoadState.Loading -> {
                Loader(
                    Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
                )
            }

            refresh is LoadState.Error || append is LoadState.Error -> {
                Box(Modifier.padding(contentPadding)) {
                    // TODO: Error view
                }
            }

            refresh is LoadState.NotLoading && searchResults.itemCount < 1 -> {
                EmptyView(
                    modifier = Modifier
                        .padding(contentPadding)
                        .fillMaxWidth(),
                    icon = {
                        Text(
                            modifier = Modifier.padding(bottom = MaterialTheme.dimens.dimen2_5),
                            text = "(·_·)",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.displayLarge,
                            fontSize = 60.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    },
                    title = stringResource(Res.string.title_no_search_result),
                    subtitle = stringResource(Res.string.subtitle_no_search_result),
                )
            }

            refresh is LoadState.NotLoading -> {
                content(contentPadding) {
                    if (append is LoadState.Loading) {
                        Loader(
                            Modifier
                                .fillMaxWidth()
                                .padding(MaterialTheme.dimens.dimen3, MaterialTheme.dimens.dimen2)
                        )
                    }
                }
            }
        }
    }
}
