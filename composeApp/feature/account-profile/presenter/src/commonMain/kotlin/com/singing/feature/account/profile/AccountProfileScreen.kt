package com.singing.feature.account.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.navigator.currentOrThrow
import com.singing.app.common.views.shared.record.dialog.RecordPlayDialog
import com.singing.app.common.views.toPlayerController
import com.singing.app.common.views.toRecordCardData
import com.singing.app.common.views.toUserUiData
import com.singing.app.domain.model.Publication
import com.singing.app.feature.rememberRecordPlayer
import com.singing.app.navigation.AppNavigator
import com.singing.app.navigation.SharedScreen
import com.singing.app.ui.screen.dimens
import com.singing.app.ui.screen.listSpacing
import com.singing.feature.account.profile.viewmodel.AccountProfileIntent
import com.singing.feature.account.profile.viewmodel.AccountProfileUiState
import com.singing.feature.account.profile.views.AccountBanner
import com.singing.feature.account.profile.views.AccountPublications


@Composable
fun AccountProfileScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: AccountProfileViewModel,
    uiState: AccountProfileUiState,
) {
    val publications = viewModel.publications.collectAsLazyPagingItems()

    val navigator = AppNavigator.currentOrThrow

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

    LazyVerticalStaggeredGrid(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalItemSpacing = MaterialTheme.dimens.listSpacing,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.listSpacing),
        columns = StaggeredGridCells.Adaptive(minSize = 300.dp),
    ) {
        item(span = StaggeredGridItemSpan.FullLine) {
            AccountBanner(
                currentUser = uiState.user,
                account = uiState.account,
                userInfo = uiState.userInfo,
                onLogout = {
                    viewModel.onIntent(AccountProfileIntent.Logout)

                    navigator.popToRoot()
                }
            )

            Spacer(Modifier.height(MaterialTheme.dimens.dimen1_5))
        }

        AccountPublications(
            account = uiState.account,
            publications = publications,
            playPublication = {
                publicationToPlay = it
            },
            openPublicationDetails = {
                navigator.navigate(SharedScreen.PublicationDetails(it))
            }
        )
    }
}
