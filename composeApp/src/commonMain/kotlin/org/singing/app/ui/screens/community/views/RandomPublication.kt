package org.singing.app.ui.screens.community.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.baseline_shuffle_variant_24
import com.singing.app.composeapp.generated.resources.title_random_latest_publication
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.domain.model.Publication
import org.singing.app.ui.common.player.RecordPlayer
import org.singing.app.ui.views.base.list.Loader
import org.singing.app.ui.views.base.publication.PublicationCardWithPlayer
import org.singing.app.ui.views.base.publication.publicationCardAppearance


@Composable
fun RandomPublication(
    modifier: Modifier,
    shape: Shape,
    player: RecordPlayer,
    publication: Publication?,
    isLoading: Boolean,
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

        if (isLoading || publication == null) {
            Loader(color = MaterialTheme.colorScheme.primary)
        } else {
            PublicationCardWithPlayer(
                modifier = modifier,
                publication = publication,
                player = player,
                onAuthorClick = { onAuthorClick(publication) },
                navigatePublicationDetails = { navigatePublicationDetails(publication) },
            )
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
