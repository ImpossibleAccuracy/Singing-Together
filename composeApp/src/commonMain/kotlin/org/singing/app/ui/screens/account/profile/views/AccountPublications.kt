package org.singing.app.ui.screens.account.profile.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.singing.app.domain.model.Publication
import org.singing.app.ui.views.base.publication.PublicationCard

@Composable
fun AccountPublications(
    modifier: Modifier = Modifier,
    publications: List<Publication>,
    onAuthorClick: (Publication) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        columns = GridCells.Adaptive(
            minSize = 380.dp,
        ),
    ) {
        items(publications) {
            PublicationCard(
                publication = it,
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                onAuthorClick = {
                    onAuthorClick(it)
                }
            )
        }
    }
}
