package com.singing.feature.main.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.singing.app.common.views.base.list.EmptyView
import com.singing.app.common.views.base.list.Loader
import com.singing.app.common.views.base.publication.PublicationCard
import com.singing.app.common.views.base.publication.PublicationCardWithPlayer
import com.singing.app.common.views.base.publication.publicationCardAppearance
import com.singing.app.common.views.model.actions.PublicationCardActions
import com.singing.app.common.views.shared.record.dialog.RecordPlayDialog
import com.singing.app.common.views.toPlayerController
import com.singing.app.common.views.toPublicationCardData
import com.singing.app.common.views.toRecordCardData
import com.singing.app.common.views.toUserUiData
import com.singing.app.domain.features.RecordPlayer
import com.singing.app.domain.model.DataState
import com.singing.app.domain.model.Publication
import com.singing.app.feature.rememberRecordPlayer
import com.singing.app.navigation.SharedScreen
import com.singing.app.ui.screen.dimens
import com.singing.app.ui.screen.listSpacing
import com.singing.feature.main.presenter.generated.resources.Res
import com.singing.feature.main.presenter.generated.resources.common_error_subtitle
import com.singing.feature.main.presenter.generated.resources.common_error_title
import com.singing.feature.main.presenter.generated.resources.subtitle_empty_publications
import com.singing.feature.main.presenter.generated.resources.title_empty_publications
import com.singing.feature.main.presenter.generated.resources.title_recent_publications
import kotlinx.collections.immutable.PersistentList
import org.jetbrains.compose.resources.stringResource


@Composable
fun RecentPublicationsListContainer(
    modifier: Modifier = Modifier,
    listModifier: Modifier = Modifier,
    latestPublications: DataState<PersistentList<Publication>>,
    navigate: (SharedScreen) -> Unit
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

    RecentPublicationsList(
        modifier = modifier,
        listModifier = listModifier,
        latestPublications = latestPublications,
        player = mainRecordPlayer,
        playPublication = {
            publicationToPlay = it
        },
        onAuthorClick = {
            navigate(SharedScreen.UserProfile(it.author))
        },
        navigatePublicationDetails = {
            navigate(SharedScreen.PublicationDetails(it))
        },
    )
}


@Composable
fun RecentPublicationsList(
    modifier: Modifier = Modifier,
    listModifier: Modifier = Modifier,
    player: RecordPlayer,
    latestPublications: DataState<PersistentList<Publication>>,
    playPublication: (Publication) -> Unit,
    onAuthorClick: (Publication) -> Unit,
    navigatePublicationDetails: (Publication) -> Unit,
) {
    Column(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.medium)
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(
                start = MaterialTheme.dimens.dimen2,
                top = MaterialTheme.dimens.dimen2,
                end = MaterialTheme.dimens.dimen2,
                bottom = MaterialTheme.dimens.dimen2,
            ),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1_5),
    ) {
        when (latestPublications) {
            DataState.Empty -> {
                EmptyView(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(Res.string.title_empty_publications),
                    subtitle = stringResource(Res.string.subtitle_empty_publications),
                )
            }

            is DataState.Error -> {
                EmptyView(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(Res.string.common_error_title),
                    subtitle = stringResource(Res.string.common_error_subtitle),
                )
            }

            DataState.Loading -> {
                Loader(Modifier.padding(MaterialTheme.dimens.dimen3))
            }

            is DataState.Success -> {
                Text(
                    text = stringResource(Res.string.title_recent_publications),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                )

                PublicationsList(
                    listModifier = listModifier,
                    publications = latestPublications.data,
                    player = player,
                    playPublication = playPublication,
                    onAuthorClick = onAuthorClick,
                    navigatePublicationDetails = navigatePublicationDetails,
                )
            }
        }
    }
}

@Composable
private fun PublicationsList(
    listModifier: Modifier,
    publications: PersistentList<Publication>,
    player: RecordPlayer,
    playPublication: (Publication) -> Unit,
    onAuthorClick: (Publication) -> Unit,
    navigatePublicationDetails: (Publication) -> Unit
) {
    LazyColumn(
        modifier = listModifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.listSpacing),
    ) {
        item {
            val item = publications.first()

            PublicationCardWithPlayer(
                modifier = publicationCardAppearance(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                ),
                contentColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor = MaterialTheme.colorScheme.primaryContainer,
                playerController = player.toPlayerController(item.record),
                data = item.toPublicationCardData(true),
                actions = PublicationCardActions(
                    onPlay = {
                        playPublication.invoke(item)
                    },
                    onAuthorClick = {
                        onAuthorClick(item)
                    },
                    navigatePublicationDetails = {
                        navigatePublicationDetails(item)
                    },
                ),
            )
        }

        if (publications.size > 1) {
            items(publications.size - 1) { index ->
                val item = publications[index + 1]

                PublicationCard(
                    modifier = publicationCardAppearance(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    ),
                    data = item.toPublicationCardData(true),
                    actions = PublicationCardActions(
                        onPlay = {
                            playPublication.invoke(item)
                        },
                        onAuthorClick = {
                            onAuthorClick(item)
                        },
                        navigatePublicationDetails = {
                            navigatePublicationDetails(item)
                        },
                    ),
                )
            }
        }
    }
}
