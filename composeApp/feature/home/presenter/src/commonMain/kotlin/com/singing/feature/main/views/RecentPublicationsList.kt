package com.singing.feature.main.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.singing.app.common.views.base.list.EmptyView
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
import com.singing.app.domain.model.Publication
import com.singing.app.feature.rememberRecordPlayer
import com.singing.app.navigation.SharedScreen
import com.singing.feature.main.presenter.generated.resources.Res
import com.singing.feature.main.presenter.generated.resources.subtitle_empty_publications
import com.singing.feature.main.presenter.generated.resources.title_empty_publications
import com.singing.feature.main.presenter.generated.resources.title_recent_publications
import com.singing.feature.main.viewmodel.MainUiState
import kotlinx.collections.immutable.PersistentList
import org.jetbrains.compose.resources.stringResource


@Composable
fun RecentPublicationsListContainer(
    uiState: MainUiState,
    navigate: (SharedScreen) -> Unit
) {
    val mainRecordPlayer = rememberRecordPlayer()

    var publicationToPlay by remember { mutableStateOf<Publication?>(null) }

    if (publicationToPlay != null) {
        val dialogRecordPlayer = rememberRecordPlayer()

        RecordPlayDialog(
            playerController = dialogRecordPlayer.toPlayerController(publicationToPlay!!.record),
            record = publicationToPlay!!.record.toRecordCardData(),
            creator = uiState.user?.toUserUiData(),
            onDismiss = {
                publicationToPlay = null
            }
        )
    }

    RecentPublicationsList(
        modifier = Modifier.fillMaxWidth(),
        publications = uiState.latestPublications,
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
    publications: PersistentList<Publication>,
    playPublication: (Publication) -> Unit,
    onAuthorClick: (Publication) -> Unit,
    navigatePublicationDetails: (Publication) -> Unit,
) {
    Column(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.medium)
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .padding(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 16.dp,
            ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (publications.isNotEmpty()) {
            Text(
                text = stringResource(Res.string.title_recent_publications),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
            )
        }

        if (publications.isEmpty()) {
            EmptyView(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(Res.string.title_empty_publications),
                subtitle = stringResource(Res.string.subtitle_empty_publications),
            )
        } else {
            PublicationsList(
                listModifier = listModifier,
                publications = publications,
                player = player,
                playPublication = playPublication,
                onAuthorClick = onAuthorClick,
                navigatePublicationDetails = navigatePublicationDetails,
            )
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
        modifier = listModifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
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
