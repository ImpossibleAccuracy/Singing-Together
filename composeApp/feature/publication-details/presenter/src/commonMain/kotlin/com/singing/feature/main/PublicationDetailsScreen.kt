package com.singing.feature.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.navigator.currentOrThrow
import com.singing.app.common.views.base.publication.PublicationCard
import com.singing.app.common.views.base.publication.publicationCardAppearance
import com.singing.app.common.views.model.actions.PublicationCardActions
import com.singing.app.common.views.shared.player.PlayerView
import com.singing.app.common.views.toPlayerController
import com.singing.app.common.views.toPublicationCardData
import com.singing.app.feature.rememberRecordPlayer
import com.singing.app.navigation.AppNavigator
import com.singing.app.navigation.SharedScreen
import com.singing.app.ui.screen.dimens
import com.singing.feature.main.viewmodel.PublicationDetailsUiState
import com.singing.feature.record.views.RecordDetails
import com.singing.feature.record.views.RecordDetailsData


@Composable
fun PublicationDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: PublicationDetailsViewModel,
    uiState: PublicationDetailsUiState,
) {
    val navigator = AppNavigator.currentOrThrow

    Row(
        modifier = modifier.verticalScroll(state = rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen2),
    ) {
        val recordPoints = viewModel.recordPoints.collectAsLazyPagingItems()

        RecordDetails(
            modifier = Modifier.weight(7f),
            data = RecordDetailsData(
                user = uiState.publication.author,
                record = uiState.publication.record,
                player = null,
                editable = false,
                recordPoints = recordPoints,
                note = viewModel::getNote,
            ),
            actions = null,
        )

        Column(
            modifier = Modifier.weight(3f),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen2),
        ) {
            PublicationCard(
                modifier = publicationCardAppearance(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                ),
                data = uiState.publication.toPublicationCardData(false),
                actions = PublicationCardActions(
                    onAuthorClick = {
                        navigator.navigate(
                            SharedScreen.UserProfile(
                                uiState.publication.author
                            )
                        )
                    },
                    navigatePublicationDetails = {}
                )
            )

            val player = rememberRecordPlayer()

            PlayerView(
                modifier = modifier,
                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                contentColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                playerController = player.toPlayerController(uiState.publication.record)
            )
        }
    }
}
