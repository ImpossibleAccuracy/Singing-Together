package org.singing.app.ui.screens.account.profile

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.singing.app.di.module.viewModels
import org.singing.app.domain.model.AccountUiData
import org.singing.app.domain.model.Publication
import org.singing.app.setup.collectAsStateSafe
import org.singing.app.ui.base.Divider
import org.singing.app.ui.base.connectVerticalNestedScroll
import org.singing.app.ui.common.ContentContainer
import org.singing.app.ui.common.player.RecordPlayer
import org.singing.app.ui.common.player.rememberRecordPlayer
import org.singing.app.ui.screens.account.profile.views.AccountBanner
import org.singing.app.ui.screens.account.profile.views.AccountPublications
import org.singing.app.ui.screens.publication.details.PublicationDetailsScreen
import org.singing.app.ui.views.shared.record.dialog.RecordPlayDialog

data class AccountProfileScreen(
    val requestedAccount: AccountUiData,
) : Screen {
    @Composable
    override fun Content() {
        val viewModel = viewModels<AccountProfileViewModel>()
        val recordPlayer = rememberRecordPlayer()

        val accountInfo by viewModel.accountInfo.collectAsStateSafe()

        val verticalScroll = rememberScrollState()

        LaunchedEffect(requestedAccount) {
            viewModel.load(requestedAccount)
        }

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
                        start = 12.dp,
                        end = 12.dp,
                    ),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                AccountBanner(
                    modifier = Modifier.fillMaxWidth(),
                    account = requestedAccount,
                    accountInfo = accountInfo,
                )

                Divider(Modifier.padding(horizontal = 24.dp))

                PublicationsContainer(
                    pageScrollState = verticalScroll,
                    recordPlayer = recordPlayer,
                    viewModel = viewModel,
                )
            }
        }
    }

    @Composable
    private fun PublicationsContainer(
        pageScrollState: ScrollState,
        recordPlayer: RecordPlayer,
        viewModel: AccountProfileViewModel,
    ) {
        val navigator = LocalNavigator.currentOrThrow

        val publications by viewModel.publication.collectAsStateSafe()

        var publicationToPlay by remember { mutableStateOf<Publication?>(null) }

        if (publicationToPlay != null) {
            val dialogRecordPlayer = rememberRecordPlayer()

            RecordPlayDialog(
                player = dialogRecordPlayer,
                record = publicationToPlay!!.record,
                author = publicationToPlay!!.author,
                onDismiss = {
                    publicationToPlay = null
                }
            )
        }

        AccountPublications(
            modifier = Modifier.fillMaxWidth(),
            listModifier = Modifier.connectVerticalNestedScroll(10000.dp, pageScrollState),
            account = requestedAccount,
            recordPlayer = recordPlayer,
            publications = publications,
            playPublication = {
                publicationToPlay = it
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
}
