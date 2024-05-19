package org.singing.app.ui.screens.community.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.subtitle_no_search_result
import com.singing.app.composeapp.generated.resources.title_no_search_result
import kotlinx.collections.immutable.PersistentList
import org.jetbrains.compose.resources.stringResource
import org.singing.app.domain.model.Publication
import org.singing.app.ui.base.onVisibilityChange
import org.singing.app.ui.common.player.RecordPlayer
import org.singing.app.ui.views.base.list.EmptyView
import org.singing.app.ui.views.base.list.Loader
import org.singing.app.ui.views.base.publication.PublicationCard
import org.singing.app.ui.views.base.publication.publicationCardAppearance
import org.singing.app.ui.views.shared.publication.MainPublicationCard

@Composable
fun PublicationSearchResult(
    modifier: Modifier = Modifier,
    gridModifier: Modifier = Modifier,
    player: RecordPlayer,
    publications: PersistentList<Publication>,
    isLoaderVisible: Boolean = true,
    playPublication: (Publication) -> Unit,
    onAuthorClick: (Publication) -> Unit,
    navigatePublicationDetails: (Publication) -> Unit,
    onLoadMorePublications: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (publications.isNotEmpty()) {
            PublicationsGrid(
                gridModifier = gridModifier,
                player = player,
                publications = publications,
                playPublication = playPublication,
                onAuthorClick = onAuthorClick,
                navigatePublicationDetails = navigatePublicationDetails
            )
        }

        if (isLoaderVisible) {
            ResultLoader(onLoadMorePublications)
        } else if (publications.isEmpty()) {
            EmptyView(
                modifier = Modifier.fillMaxWidth(),
                icon = {
                    Text(
                        modifier = Modifier.padding(bottom = 20.dp),
                        text = "(·_·)",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.displayLarge,
                        fontSize = 60.sp,
                        fontWeight = FontWeight.Bold,
                    )
                },
                title = stringResource(Res.string.title_no_search_result),
                subtitle = stringResource(Res.string.subtitle_no_search_result),
            )
        } else {
            // TODO: add "AllRead" View
        }
    }
}

@Composable
private fun PublicationsGrid(
    gridModifier: Modifier,
    player: RecordPlayer,
    publications: PersistentList<Publication>,
    playPublication: (Publication) -> Unit,
    onAuthorClick: (Publication) -> Unit,
    navigatePublicationDetails: (Publication) -> Unit
) {
    LazyVerticalStaggeredGrid(
        modifier = gridModifier,
        columns = StaggeredGridCells.Adaptive(minSize = 360.dp),
        verticalItemSpacing = 12.dp,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(span = StaggeredGridItemSpan.FullLine) {
            val mainPublication = publications.first()

            MainPublicationCard(
                player = player,
                publication = mainPublication,
                onAuthorClick = { onAuthorClick(mainPublication) },
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
                    onPlay = {
                        playPublication(item)
                    },
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
