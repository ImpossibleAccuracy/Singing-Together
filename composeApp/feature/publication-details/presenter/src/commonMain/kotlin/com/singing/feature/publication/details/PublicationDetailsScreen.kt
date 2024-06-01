package com.singing.feature.publication.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.navigator.currentOrThrow
import com.singing.app.common.views.base.list.EmptyView
import com.singing.app.common.views.base.list.Loader
import com.singing.app.common.views.base.publication.PublicationCard
import com.singing.app.common.views.base.publication.publicationCardAppearance
import com.singing.app.common.views.model.actions.PublicationCardActions
import com.singing.app.common.views.shared.player.PlayerView
import com.singing.app.common.views.toPlayerController
import com.singing.app.common.views.toPublicationCardData
import com.singing.app.domain.model.DataState
import com.singing.app.feature.rememberPublicationCardActions
import com.singing.app.feature.rememberRecordPlayer
import com.singing.app.navigation.AppNavigator
import com.singing.app.navigation.SharedScreen
import com.singing.app.ui.screen.dimens
import com.singing.feature.publication.details.presenter.generated.resources.Res
import com.singing.feature.publication.details.presenter.generated.resources.common_error_subtitle
import com.singing.feature.publication.details.presenter.generated.resources.common_error_title
import com.singing.feature.publication.details.presenter.generated.resources.common_no_data_subtitle
import com.singing.feature.publication.details.presenter.generated.resources.common_no_data_title
import com.singing.feature.publication.details.viewmodel.PublicationDetailsIntent
import com.singing.feature.publication.details.viewmodel.PublicationDetailsUiState
import com.singing.feature.record.views.RecordDetails
import com.singing.feature.record.views.RecordDetailsActions
import com.singing.feature.record.views.RecordDetailsData
import org.jetbrains.compose.resources.stringResource


@Composable
fun PublicationDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: PublicationDetailsViewModel,
    uiState: PublicationDetailsUiState,
) {
    val navigator = AppNavigator.currentOrThrow

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen2),
    ) {
        val recordPoints = viewModel.recordPoints.collectAsLazyPagingItems()

        when (val state = uiState.publication) {
            DataState.Empty -> {
                EmptyView(
                    title = stringResource(Res.string.common_no_data_title),
                    subtitle = stringResource(Res.string.common_no_data_subtitle),
                )
            }

            is DataState.Error -> {
                EmptyView(
                    title = stringResource(Res.string.common_error_title),
                    subtitle = stringResource(Res.string.common_error_subtitle),
                )
            }

            DataState.Loading -> {
                Loader(Modifier.fillMaxSize())
            }

            is DataState.Success -> {
                RecordDetails(
                    modifier = Modifier
                        .weight(7f)
                        .verticalScroll(rememberScrollState()),
                    data = RecordDetailsData(
                        user = state.data.author,
                        record = state.data.record,
                        player = null,
                        isRecordPointsStatic = true,
                        recordPoints = recordPoints,
                        note = viewModel::getNote,
                    ),
                    actions = RecordDetailsActions(
                        deleteRecord = {
                            viewModel.onIntent(
                                PublicationDetailsIntent.DeletePublication(state.data)
                            )

                            navigator.pop()
                        },
                    ),
                    availableActions = rememberPublicationCardActions(uiState.user, state.data),
                )

                Column(
                    modifier = Modifier
                        .weight(3f)
                        .verticalScroll(state = rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen2),
                ) {
                    PublicationCard(
                        modifier = publicationCardAppearance(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        ),
                        data = state.data.toPublicationCardData(false),
                        actions = PublicationCardActions(
                            onAuthorClick = {
                                navigator.navigate(
                                    SharedScreen.UserProfile(state.data.author)
                                )
                            },
                            navigatePublicationDetails = {}
                        )
                    )

                    val player = rememberRecordPlayer()

                    PlayerView(
                        modifier = Modifier,
                        containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        contentColor = MaterialTheme.colorScheme.secondary,
                        inactiveTrackColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                        playerController = player.toPlayerController(state.data.record)
                    )
                }
            }
        }
    }
}
