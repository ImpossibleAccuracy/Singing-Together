package com.singing.app.feature.community

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.navigator.currentOrThrow
import com.singing.app.feature.community.viewmodel.CommunityUiState
import com.singing.app.feature.community.views.PopularCategories
import com.singing.app.feature.community.views.RandomPublicationContainer
import com.singing.app.feature.community.views.WelcomeView
import com.singing.app.feature.community.views.search.PublicationSearchFiltersContainer
import com.singing.app.feature.community.views.search.PublicationSearchResultContainer
import com.singing.app.navigation.AppNavigator
import com.singing.app.navigation.SharedScreen
import com.singing.app.navigation.views.ContentContainer
import com.singing.app.ui.screen.dimens


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
            modifier = modifier.verticalScroll(state = verticalScroll),
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen3),
            ) {
                PopularCategories(
                    modifier = Modifier.weight(5f),
                    shape = sectionShape,
                    categories = uiState.popularPublicationTags,
                )

                RandomPublicationContainer(
                    modifier = Modifier.weight(4f),
                    sectionShape = sectionShape,
                    uiState = uiState,
                    navigate = {
                        navigator.navigate(it)
                    },
                    newIntent = {
                        viewModel.onIntent(it)
                    }
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen3),
            ) {
                PublicationSearchFiltersContainer(
                    modifier = Modifier.fillMaxWidth(),
                    uiState = uiState,
                    newIntent = {
                        viewModel.onIntent(it)
                    }
                )

                PublicationSearchResultContainer(
                    modifier = Modifier.fillMaxWidth(),
                    uiState = uiState,
                    searchResults = viewModel.searchResults.collectAsLazyPagingItems(),
                    navigate = {
                        navigator.navigate(it)
                    },
                )
            }
        }
    }
}
