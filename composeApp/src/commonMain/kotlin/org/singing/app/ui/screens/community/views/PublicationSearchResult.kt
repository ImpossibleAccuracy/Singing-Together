package org.singing.app.ui.screens.community.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import org.singing.app.domain.model.Publication
import org.singing.app.ui.base.onVisibilityChange
import org.singing.app.ui.common.player.RecordPlayer
import org.singing.app.ui.views.base.Loader
import org.singing.app.ui.views.base.publication.PublicationCard
import org.singing.app.ui.views.base.publication.publicationCardAppearance
import org.singing.app.ui.views.shared.publication.MainPublicationCard

@Composable
fun PublicationSearchResult(
    modifier: Modifier = Modifier,
    gridModifier: Modifier = Modifier,
    player: RecordPlayer,
    publications: ImmutableList<Publication>,
    isLoaderVisible: Boolean = true,
    navigatePublicationDetails: (Publication) -> Unit,
    onLoadMorePublications: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (publications.isEmpty()) {
            // TODO: add EmptyView
        } else {
            PublicationsGrid(
                gridModifier = gridModifier,
                player = player,
                publications = publications,
                navigatePublicationDetails = navigatePublicationDetails
            )
        }

        if (isLoaderVisible) {
            ResultLoader(onLoadMorePublications)
        } else {
            // TODO: add "AllRead" View
        }
    }
}

@Composable
private fun PublicationsGrid(
    gridModifier: Modifier,
    player: RecordPlayer,
    publications: ImmutableList<Publication>,
    navigatePublicationDetails: (Publication) -> Unit
) {
    LazyVerticalStaggeredGrid(
        modifier = gridModifier,
        columns = StaggeredGridCells.Adaptive(minSize = 386.dp),
        verticalItemSpacing = 12.dp,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(span = StaggeredGridItemSpan.FullLine) {
            val mainPublication = publications.first()

            MainPublicationCard(
                modifier = Modifier.fillMaxWidth(),
                player = player,
                publication = mainPublication,
                onNavigateToDetails = {
                    navigatePublicationDetails(mainPublication)
                }
            )
        }

        if (publications.size > 1) {
            items(publications) { item ->
                PublicationCard(
                    modifier = publicationCardAppearance(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    ),
                    publication = item,
                    navigatePublicationDetails = {
                        navigatePublicationDetails(item)
                    }
                )
            }
        }
    }
}

@Composable
private fun ResultLoader(onLoadMorePublications: () -> Unit) {
    Loader(
        modifier = Modifier.onVisibilityChange {
            if (it) {
                onLoadMorePublications()
            }
        }
    )
}
