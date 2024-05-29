package com.singing.feature.account.profile.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import app.cash.paging.compose.itemKey
import com.singing.app.common.views.base.list.EmptyView
import com.singing.app.common.views.base.list.Loader
import com.singing.app.common.views.base.publication.PublicationCard
import com.singing.app.common.views.model.actions.PublicationCardActions
import com.singing.app.common.views.shared.publication.MainPublicationCard
import com.singing.app.common.views.toPlayerController
import com.singing.app.common.views.toPublicationCardData
import com.singing.app.common.views.toRecordCardData
import com.singing.app.domain.features.RecordPlayer
import com.singing.app.domain.model.Publication
import com.singing.app.domain.model.UserData
import com.singing.app.ui.screen.dimens
import com.singing.app.ui.screen.largeIcon
import com.singing.feature.account.profile.presenter.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Suppress("FunctionName")
fun LazyListScope.AccountPublications(
    account: UserData,
    recordPlayer: RecordPlayer,
    publications: LazyPagingItems<Publication>,
    playPublication: (Publication) -> Unit,
    openPublicationDetails: (Publication) -> Unit,
) {
    with(publications.loadState) {
        when {
            refresh is LoadState.Loading -> {
                item {
                    Loader()
                }
            }

            refresh is LoadState.Error || append is LoadState.Error -> {
                item {
                    EmptyView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.errorContainer)
                            .padding(
                                horizontal = 16.dp,
                                vertical = 12.dp
                            ),
                        icon = {
                            Icon(
                                modifier = Modifier.size(MaterialTheme.dimens.largeIcon),
                                imageVector = vectorResource(Res.drawable.baseline_error_outline_24),
                                tint = MaterialTheme.colorScheme.error,
                                contentDescription = "",
                            )
                        },
                        title = stringResource(Res.string.common_error_title),
                        subtitle = stringResource(Res.string.common_error_subtitle),
                    )
                }
            }

            refresh is LoadState.NotLoading && publications.itemCount < 1 -> {
                item {
                    NoPublications(account = account)
                }
            }

            refresh is LoadState.NotLoading -> {
                item(
                    key = publications.itemKey { it.id },
                    contentType = publications.itemContentType { "MAIN" }) {
                    val item = publications[0]!!

                    MainPublicationCard(
                        playerController = recordPlayer.toPlayerController(item.record),
                        recordUiData = item.record.toRecordCardData(),
                        publicationData = item.toPublicationCardData(false),
                        actions = PublicationCardActions(
                            onPlay = {
                                playPublication(item)
                            },
                            navigatePublicationDetails = {
                                openPublicationDetails(item)
                            }
                        ),
                    )
                }


                if (publications.itemCount > 1) {
                    items(
                        count = publications.itemCount - 1,
                        key = publications.itemKey { it.id },
                        contentType = publications.itemContentType(),
                    ) { index ->
                        val item = publications[index + 1]!!

                        PublicationCard(
                            data = item.toPublicationCardData(false),
                            actions = PublicationCardActions(
                                onPlay = {
                                    playPublication(item)
                                },
                                navigatePublicationDetails = {
                                    openPublicationDetails(item)
                                }
                            ),
                        )
                    }
                }

                if (append is LoadState.Loading) {
                    item {
                        Loader(
                            Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = MaterialTheme.dimens.dimen3,
                                    vertical = MaterialTheme.dimens.dimen2
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NoPublications(
    modifier: Modifier = Modifier,
    account: UserData,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center,
    ) {
        EmptyView(
            modifier = Modifier
                .widthIn(min = 300.dp)
                .fillMaxWidth(0.6f),
            title = stringResource(Res.string.empty_account_publications_title),
            subtitle = stringResource(Res.string.empty_account_publications_subtitle, account.username),
        )
    }
}
