package org.singing.app.ui.screens.account.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.launch
import org.singing.app.di.module.viewModels
import org.singing.app.domain.model.AccountUiData
import org.singing.app.setup.collectAsStateSafe
import org.singing.app.ui.base.AppScreen
import org.singing.app.ui.base.Space
import org.singing.app.ui.base.connectVerticalNestedScroll
import org.singing.app.ui.screens.account.profile.views.AccountBanner
import org.singing.app.ui.screens.account.profile.views.AccountPublications
import org.singing.app.ui.screens.publication.details.PublicationDetailsScreen
import org.singing.app.ui.views.shared.publication.MainPublicationCard

class AccountProfileScreen(
    private val requestedAccount: AccountUiData,
) : AppScreen() {
    @Composable
    override fun Content() {
        val viewModel = viewModels<AccountProfileViewModel>()
        val navigator = LocalNavigator.currentOrThrow
        val coroutineScope = rememberCoroutineScope()

        val accountInfo by viewModel.accountInfo.collectAsStateSafe()
        val publications by viewModel.publication.collectAsStateSafe()

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
                    ),
            ) {
                AccountBanner(
                    modifier = Modifier.fillMaxWidth(),
                    account = requestedAccount,
                    accountInfo = accountInfo,
                )

                Space(24.dp)

                if (publications.isEmpty()) {
                    // TODO: replace with EmptyView
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "${requestedAccount.username} didn't publish anything.",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }
                } else {
                    val mainPublication = publications.first()

                    Text(
                        text = "Publications",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleMedium,
                    )

                    Space(8.dp)

                    MainPublicationCard(
                        publication = mainPublication,
                        player = viewModel.recordPlayer,
                    )

                    if (publications.size > 1) {
                        Space(12.dp)

                        AccountPublications(
                            modifier = Modifier.connectVerticalNestedScroll(10000.dp, verticalScroll),
                            publications = publications.subList(1, publications.size),
                            onAuthorClick = {
                                if (it.author.id == requestedAccount.id) {
                                    coroutineScope.launch {
                                        verticalScroll.animateScrollTo(0)
                                    }
                                } else {
                                    navigator.push(
                                        AccountProfileScreen(
                                            requestedAccount = it.author,
                                        )
                                    )
                                }
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
            }
        }
    }
}
