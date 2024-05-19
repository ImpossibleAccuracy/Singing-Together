package org.singing.app.ui.views.base.publication

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.rememberImagePainter
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.baseline_person_24
import com.singing.app.composeapp.generated.resources.baseline_play_circle_filled_24
import nl.jacobras.humanreadable.HumanReadable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.domain.model.Publication
import org.singing.app.ui.base.Space
import org.singing.app.ui.base.cardAppearance


@Composable
@ReadOnlyComposable
fun publicationCardAppearance(
    containerColor: Color = MaterialTheme.colorScheme.surface,
    shape: Shape = MaterialTheme.shapes.medium,
    padding: PaddingValues = PaddingValues(12.dp)
) = Modifier.cardAppearance(
    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    shape = shape,
    background = containerColor,
    padding = padding,
)

@Composable
fun PublicationCard(
    modifier: Modifier = publicationCardAppearance(),
    publication: Publication,
    showActions: Boolean = true,
    onAuthorClick: (() -> Unit)? = null,
    navigatePublicationDetails: () -> Unit,
) {
    BasePublicationCard(
        modifier = modifier,
        authorAvatar = {
            when (publication.author.avatar) {
                null -> painterResource(Res.drawable.baseline_person_24)
                else -> rememberImagePainter(publication.author.avatar)
            }
        },
        authorUsername = publication.author.username,
        createdAt = HumanReadable.timeAgo(publication.createdAt.instant),
        description = publication.description,
        onAuthorClick = onAuthorClick,
        slotAfterAuthor = {},
        slotAfterDescription = {
            if (showActions) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AssistChip(
                        label = {
                            Text(
                                text = "See record",
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.labelLarge,
                            )
                        },
                        onClick = {
                            navigatePublicationDetails()
                        }
                    )

                    Space(8.dp)

                    AssistChip(
                        label = {
                            Text(
                                text = "Listen now",
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.labelLarge,
                            )
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = vectorResource(Res.drawable.baseline_play_circle_filled_24),
                                tint = MaterialTheme.colorScheme.onSurface,
                                contentDescription = ""
                            )
                        },
                        onClick = {

                        }
                    )
                }
            }
        }
    )
}

@Composable
fun BasePublicationCard(
    modifier: Modifier,
    authorAvatar: @Composable () -> Painter,
    authorUsername: String,
    createdAt: String,
    description: String,
    onAuthorClick: (() -> Unit)? = null,
    slotAfterAuthor: @Composable RowScope.() -> Unit,
    slotAfterDescription: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(shape = MaterialTheme.shapes.small)
                    .clickable(enabled = onAuthorClick != null) {
                        onAuthorClick?.invoke()
                    }
                    .let {
                        if (onAuthorClick == null) it
                        else it.padding(
                            horizontal = 8.dp,
                            vertical = 4.dp
                        )
                    }
            ) {
                Box(
                    modifier = Modifier.clip(shape = RoundedCornerShape(50))
                ) {
                    Image(
                        modifier = Modifier.size(size = 36.dp),
                        painter = authorAvatar(),
                        contentScale = ContentScale.Crop,
                        contentDescription = "Avatar",
                    )
                }

                Space(8.dp)

                Column {
                    Text(
                        text = authorUsername,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium,
                    )

                    Text(
                        text = createdAt,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            slotAfterAuthor()
        }

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = description,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium,
        )

        slotAfterDescription()
    }
}
