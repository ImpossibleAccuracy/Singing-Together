package com.singing.feature.community.views.search

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.singing.app.domain.model.Publication
import com.singing.app.feature.rememberRecordPlayer
import com.singing.app.navigation.SharedScreen
import com.singing.app.ui.screen.dimens
import com.singing.feature.community.presenter.generated.resources.*
import com.singing.feature.community.viewmodel.CommunityUiState
import org.jetbrains.compose.resources.stringResource


@Composable
fun PublicationSearchResultContainer(
    modifier: Modifier = Modifier,
    gridModifier: Modifier = Modifier,
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
                                    playerController = mainRecordPlayer.toPlayerController(mainPublication.record),
                                    recordUiData = mainPublication.record.toRecordCardData(),
                                    publicationData = mainPublication.toPublicationCardData(false),
                                    actions = PublicationCardActions(
                                        onAuthorClick = {
                                            navigate(
                                                SharedScreen.UserProfile(mainPublication.author)
                                            )
                                        },
                                        navigatePublicationDetails = {
                                            navigate(
                                                SharedScreen.PublicationDetails(mainPublication)
                                            )
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
                                    data = item.toPublicationCardData(true),
                                    actions = PublicationCardActions(
                                        onPlay = {
                                            publicationToPlay = item
                                        },
                                        onAuthorClick = {
                                            navigate(
                                                SharedScreen.UserProfile(item.author)
                                            )
                                        },
                                        navigatePublicationDetails = {
                                            navigate(
                                                SharedScreen.PublicationDetails(item)
                                            )
                                        },
                                    ),
                                )
                            }
                        }
                    }

                    item {
                        loader()
                    }
                }
            }
        }
    } else {
        Text(
            text = stringResource(Res.string.title_search_results_placeholder)
        )
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
                        .heightIn(min = 600.dp)
                        .padding(contentPadding)
                )
            }

            refresh is LoadState.Error || append is LoadState.Error -> {
                Box(Modifier.padding(contentPadding)) {
                    EmptyView(
                        title = stringResource(Res.string.error_search_results_title),
                        subtitle = stringResource(Res.string.error_search_results_subtitle),
                    )
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
                    title = stringResource(Res.string.empty_search_results_title),
                    subtitle = stringResource(Res.string.empty_search_results_subtitle),
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
