package org.singing.app.ui.screens.main.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.collections.immutable.ImmutableList
import org.singing.app.domain.model.Publication
import org.singing.app.ui.base.Space
import org.singing.app.ui.common.player.RecordPlayer
import org.singing.app.ui.views.base.publication.PublicationCard
import org.singing.app.ui.views.base.publication.PublicationCardWithPlayer


@Composable
fun RecentPublicationsList(
    modifier: Modifier = Modifier,
    listModifier: Modifier = Modifier,
    player: RecordPlayer,
    publications: ImmutableList<Publication>,
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
            )
    ) {
        Text(
            text = "Recent publications",
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
        )

        Space(12.dp)

        if (publications.isEmpty()) {
            // TODO:replace with EmptyView
            Text(
                text = "No publications",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyMedium,
            )
        } else {
            PublicationsList(
                listModifier = listModifier,
                publications = publications,
                player = player,
                onAuthorClick = onAuthorClick,
                navigatePublicationDetails = navigatePublicationDetails,
            )

            Space(8.dp)

            Box(
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
                        text = "See more",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
    }
}

@Composable
private fun PublicationsList(
    listModifier: Modifier,
    publications: ImmutableList<Publication>,
    player: RecordPlayer,
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
                    publication = item,
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
