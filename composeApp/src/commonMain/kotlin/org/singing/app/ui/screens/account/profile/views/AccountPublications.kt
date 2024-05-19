package org.singing.app.ui.screens.account.profile.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import org.singing.app.domain.model.AccountUiData
import org.singing.app.domain.model.Publication
import org.singing.app.ui.common.player.RecordPlayer
import org.singing.app.ui.views.base.publication.PublicationCard
import org.singing.app.ui.views.base.publication.publicationCardAppearance
import org.singing.app.ui.views.shared.publication.MainPublicationCard

@Composable
fun AccountPublications(
    modifier: Modifier = Modifier,
    listModifier: Modifier = Modifier,
    account: AccountUiData,
    recordPlayer: RecordPlayer,
    publications: ImmutableList<Publication>,
    onAuthorClick: (Publication) -> Unit,
    navigatePublicationDetails: (Publication) -> Unit,
) {
    if (publications.isEmpty()) {
        // TODO: replace with EmptyView
        Box(
            modifier = modifier.padding(24.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "${account.username} didn't publish anything.",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineSmall,
            )
        }
    } else {
        Column(modifier = modifier) {
            Text(
                text = "Publications",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
            )

            Spacer(Modifier.height(8.dp))

            Box {
                val mainPublication = publications.first()

                MainPublicationCard(
                    publication = mainPublication,
                    player = recordPlayer,
                    onNavigateToDetails = {
                        navigatePublicationDetails(mainPublication)
                    }
                )
            }

            if (publications.size > 1) {
                Spacer(Modifier.height(12.dp))

                PublicationsGrid(
                    modifier = listModifier,
                    publications = publications.subList(1, publications.size),
                    onAuthorClick = onAuthorClick,
                    navigatePublicationDetails = navigatePublicationDetails,
                )
            }
        }
    }
}

@Composable
private fun PublicationsGrid(
    modifier: Modifier,
    publications: ImmutableList<Publication>,
    onAuthorClick: (Publication) -> Unit,
    navigatePublicationDetails: (Publication) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        columns = GridCells.Adaptive(
            minSize = 380.dp,
        ),
    ) {
        items(publications) { item ->
            PublicationCard(
                modifier = publicationCardAppearance(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                ),
                publication = item,
                onAuthorClick = {
                    onAuthorClick(item)
                },
                navigatePublicationDetails = {
                    navigatePublicationDetails(item)
                }
            )
        }
    }
}
