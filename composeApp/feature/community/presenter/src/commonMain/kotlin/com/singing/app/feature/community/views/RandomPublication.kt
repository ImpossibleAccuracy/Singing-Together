package com.singing.app.feature.community.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.currentOrThrow
import com.singing.app.common.feature.rememberRecordPlayer
import com.singing.app.common.views.base.list.Loader
import com.singing.app.common.views.base.publication.PublicationCardWithPlayer
import com.singing.app.common.views.base.publication.publicationCardAppearance
import com.singing.app.common.views.model.actions.PublicationCardActions
import com.singing.app.common.views.toPlayerController
import com.singing.app.common.views.toPublicationCardData
import com.singing.app.domain.features.RecordPlayer
import com.singing.app.domain.model.DataState
import com.singing.app.domain.model.Publication
import com.singing.app.feature.community.viewmodel.CommunityIntent
import com.singing.app.feature.community.viewmodel.CommunityUiState
import com.singing.app.navigation.SharedScreen
import com.singing.app.navigation.base.LocalAppPage
import com.singing.feature.community.presenter.generated.resources.Res
import com.singing.feature.community.presenter.generated.resources.baseline_shuffle_variant_24
import com.singing.feature.community.presenter.generated.resources.title_random_latest_publication
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource


@Composable
fun RandomPublicationContainer(
    modifier: Modifier = Modifier,
    sectionShape: Shape,
    uiState: CommunityUiState,
    navigate: (SharedScreen) -> Unit,
    newIntent: (CommunityIntent) -> Unit,
) {
    val appPage = LocalAppPage.currentOrThrow
    val player = appPage.rememberRecordPlayer()

    LaunchedEffect(uiState.randomPublication) {
        player.reset()
    }

    RandomPublication(
        modifier = modifier,
        shape = sectionShape,
        player = player,
        publication = uiState.randomPublication,
        onReload = {
            newIntent(CommunityIntent.ReloadRandomPublication)
        },
        onAuthorClick = {
            navigate(
                SharedScreen.UserProfile(it.author)
            )
        },
        navigatePublicationDetails = {
            navigate(
                SharedScreen.PublicationDetails(it)
            )
        },
    )
}

@Composable
fun RandomPublication(
    modifier: Modifier,
    shape: Shape,
    player: RecordPlayer,
    publication: DataState<Publication>,
    onReload: () -> Unit,
    onAuthorClick: (Publication) -> Unit,
    navigatePublicationDetails: (Publication) -> Unit,
) {
    Column(
        modifier = publicationCardAppearance(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            shape = shape,
            padding = PaddingValues(
                start = 16.dp,
                top = 4.dp,
                end = 16.dp,
                bottom = 16.dp,
            ),
        ),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        RandomPublicationHeader(onReload)

        when (publication) {
            DataState.Loading -> {
                Loader(color = MaterialTheme.colorScheme.primary)
            }

            is DataState.Error -> {
                // TODO: add ErrorView
            }

            is DataState.Empty -> {
                // TODO: add EmptyView
            }

            is DataState.Success -> {
                PublicationCardWithPlayer(
                    modifier = modifier,
                    playerController = player.toPlayerController(publication.data.record),
                    data = publication.data.toPublicationCardData(false),
                    actions = PublicationCardActions(
                        onAuthorClick = { onAuthorClick(publication.data) },
                        navigatePublicationDetails = { navigatePublicationDetails(publication.data) },
                    ),
                )
            }
        }
    }
}

@Composable
private fun RandomPublicationHeader(onReload: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(Res.string.title_random_latest_publication),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Black,
        )

        Spacer(Modifier.weight(1f))

        IconButton(
            onClick = onReload,
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.baseline_shuffle_variant_24),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = "",
            )
        }
    }
}
