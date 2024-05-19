package org.singing.app.ui.screens.main.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.subtitle_empty_publications
import com.singing.app.composeapp.generated.resources.title_empty_publications
import com.singing.app.composeapp.generated.resources.title_recent_publications
import kotlinx.collections.immutable.PersistentList
import org.jetbrains.compose.resources.stringResource
import org.singing.app.domain.model.Publication
import org.singing.app.ui.common.player.RecordPlayer
import org.singing.app.ui.views.base.list.EmptyView
import org.singing.app.ui.views.base.publication.PublicationCard
import org.singing.app.ui.views.base.publication.PublicationCardWithPlayer
import org.singing.app.ui.views.base.publication.publicationCardAppearance


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

            // TODO
            /*Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                TextButton(
                    modifier = Modifier.widthIn(min = 180.dp),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary,
                    ),
                    onClick = {}
                ) {
                    Text(
                        text = stringResource(Res.string.action_see_more),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }*/
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
                publication = item,
                player = player,
                contentColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor = MaterialTheme.colorScheme.primaryContainer,
                onAuthorClick = {
                    onAuthorClick(item)
                },
                navigatePublicationDetails = {
                    navigatePublicationDetails(item)
                },
            )
        }

        if (publications.size > 1) {
            items(publications.size - 1) { index ->
                val item = publications[index + 1]

                PublicationCard(
                    modifier = publicationCardAppearance(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    ),
                    publication = item,
                    onPlay = {
                        playPublication.invoke(item)
                    },
                    onAuthorClick = {
                        onAuthorClick(item)
                    },
                    navigatePublicationDetails = {
                        navigatePublicationDetails(item)
                    },
                )
            }
        }
    }
}
