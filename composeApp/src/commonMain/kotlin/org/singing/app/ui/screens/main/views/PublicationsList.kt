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
import androidx.compose.ui.unit.dp
import org.singing.app.di.injectComponent
import org.singing.app.domain.model.Publication
import org.singing.app.ui.base.Space
import org.singing.app.ui.views.base.publication.PublicationCard
import org.singing.app.ui.views.base.publication.PublicationCardWithPlayer


@Composable
fun PublicationsList(
    modifier: Modifier = Modifier,
    listModifier: Modifier = Modifier,
    publications: List<Publication>,
    onAuthorClick: (Publication) -> Unit,
) {
    Column(
        modifier = modifier
            .clip(shape = MaterialTheme.shapes.medium)
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
            .padding(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 16.dp,
            )
    ) {
        Text(
            text = "Publications",
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            style = MaterialTheme.typography.titleMedium,
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
            LazyColumn(
                modifier = listModifier.fillMaxWidth()
            ) {
                item {
                    val item = publications.first()

                    PublicationCardWithPlayer(
                        publication = item,
                        player = injectComponent(),
                        contentColor = MaterialTheme.colorScheme.secondary,
                        inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                        onAuthorClick = {
                            onAuthorClick(item)
                        }
                    )

                    if (publications.size > 1) {
                        Space(12.dp)
                    }
                }

                if (publications.size > 1) {
                    items(publications.size - 1) { index ->
                        val item = publications[index + 1]

                        PublicationCard(
                            publication = item,
                            onAuthorClick = {
                                onAuthorClick(item)
                            }
                        )

                        if (index != publications.lastIndex) {
                            Space(12.dp)
                        }
                    }
                }
            }

            Space(8.dp)

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                TextButton(
                    modifier = Modifier.widthIn(min = 180.dp),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.secondary,
                    ),
                    onClick = {}
                ) {
                    Text(
                        text = "See more",
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
    }
}
