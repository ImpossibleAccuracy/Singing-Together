package org.singing.app.ui.screens.account.profile.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.label_account_no_publications
import com.singing.app.composeapp.generated.resources.title_recent_publications
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import org.jetbrains.compose.resources.stringResource
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
    publications: PersistentList<Publication>,
    playPublication: (Publication) -> Unit,
    navigatePublicationDetails: (Publication) -> Unit,
) {
    if (publications.isEmpty()) {
        NoPublications(modifier, account)
    } else {
        Column(modifier = modifier) {
            Text(
                text = stringResource(Res.string.title_recent_publications),
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

                val subList = remember(publications) {
                    publications.subList(1, publications.size)
                        .toPersistentList()
                }

                PublicationsGrid(
                    modifier = listModifier,
                    publications = subList,
                    playPublication = playPublication,
                    navigatePublicationDetails = navigatePublicationDetails,
                )
            }
        }
    }
}

@Composable
private fun NoPublications(modifier: Modifier, account: AccountUiData) {
    // TODO: replace with EmptyView
    Box(
        modifier = modifier.padding(24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(
                Res.string.label_account_no_publications,
                account.username
            ),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}

@Composable
private fun PublicationsGrid(
    modifier: Modifier,
    publications: PersistentList<Publication>,
    playPublication: (Publication) -> Unit,
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
                onPlay = {
                    playPublication(item)
                },
                navigatePublicationDetails = {
                    navigatePublicationDetails(item)
                }
            )
        }
    }
}
