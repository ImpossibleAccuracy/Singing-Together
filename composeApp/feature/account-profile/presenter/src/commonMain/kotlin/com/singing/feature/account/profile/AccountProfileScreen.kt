package com.singing.feature.account.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.navigator.currentOrThrow
import com.singing.app.feature.rememberRecordPlayer
import com.singing.app.navigation.AppNavigator
import com.singing.app.navigation.SharedScreen
import com.singing.app.ui.screen.dimens
import com.singing.feature.account.profile.viewmodel.AccountProfileUiState
import com.singing.feature.account.profile.views.AccountBanner
import com.singing.feature.account.profile.views.AccountPublications


@Composable
fun AccountProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: AccountProfileViewModel,
    uiState: AccountProfileUiState,
) {
    val publications = viewModel.publications.collectAsLazyPagingItems()
    val recordPlayer = rememberRecordPlayer()

    val navigator = AppNavigator.currentOrThrow
    val verticalScroll = rememberScrollState()


    LazyColumn(
        modifier = modifier.verticalScroll(state = verticalScroll),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen4_5),
    ) {
        item {
            AccountBanner(
                account = uiState.account,
                accountInfo = uiState.accountInfo
            )
        }

        AccountPublications(
            account = uiState.account,
            recordPlayer = recordPlayer,
            publications = publications,
            playPublication = {},
            openPublicationDetails = {
                navigator.navigate(SharedScreen.PublicationDetails(it))
            }
        )
    }
}
