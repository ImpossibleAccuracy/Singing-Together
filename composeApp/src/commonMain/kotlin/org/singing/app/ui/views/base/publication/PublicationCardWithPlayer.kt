package org.singing.app.ui.views.base.publication

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.rememberImagePainter
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.baseline_person_24
import com.singing.app.composeapp.generated.resources.baseline_play_arrow_black_24dp
import com.singing.app.composeapp.generated.resources.baseline_stop_black_24dp
import com.singing.audio.player.PlayerState
import kotlinx.coroutines.launch
import nl.jacobras.humanreadable.HumanReadable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.domain.model.Publication
import org.singing.app.setup.collectAsStateSafe
import org.singing.app.ui.base.Divider
import org.singing.app.ui.base.Space
import org.singing.app.ui.common.player.RecordPlayer
import org.singing.app.ui.views.base.progress.TimeProgress


@Composable
fun PublicationCardWithPlayer(
    modifier: Modifier = publicationCardAppearance(),
    contentColor: Color = MaterialTheme.colorScheme.primary,
    inactiveTrackColor: Color = MaterialTheme.colorScheme.primaryContainer,
    publication: Publication,
    player: RecordPlayer,
    onAuthorClick: (() -> Unit)? = null,
    navigatePublicationDetails: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    val playerState by player.state.collectAsStateSafe()
    val playerPosition by player.position.collectAsStateSafe()

    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(shape = MaterialTheme.shapes.small)
                    .clickable(enabled = onAuthorClick != null) {
                        onAuthorClick?.invoke()
                    }
                    .padding(
                        horizontal = 8.dp,
                        vertical = 4.dp
                    )
            ) {
                Box(
                    modifier = Modifier.clip(shape = RoundedCornerShape(50))
                ) {
                    Image(
                        modifier = Modifier.size(size = 36.dp),
                        painter = when (publication.author.avatar) {
                            null -> painterResource(Res.drawable.baseline_person_24)
                            else -> rememberImagePainter(publication.author.avatar)
                        },
                        contentScale = ContentScale.Crop,
                        contentDescription = "Avatar",
                    )
                }

                Space(8.dp)

                Column {
                    Text(
                        text = publication.author.username,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium,
                    )

                    Text(
                        text = HumanReadable.timeAgo(publication.createdAt.instant),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }

            Spacer(Modifier.weight(1f))

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
        }

        Space(8.dp)

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = publication.description,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium,
        )

        Space(16.dp)

        Divider()

        Space(8.dp)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        if (playerState == PlayerState.STOP) {
                            player.play(publication.record)
                        } else {
                            player.stop()
                        }
                    }
                }
            ) {
                Icon(
                    imageVector = vectorResource(
                        when (playerState) {
                            PlayerState.PLAY -> Res.drawable.baseline_stop_black_24dp
                            PlayerState.STOP -> Res.drawable.baseline_play_arrow_black_24dp
                        }
                    ),
                    tint = contentColor,
                    contentDescription = "",
                )
            }

            Space(4.dp)

            TimeProgress(
                modifier = Modifier.weight(1f),
                contentColor = contentColor,
                inactiveTrackColor = inactiveTrackColor,
                editable = true,
                totalDuration = publication.record.duration,
                currentPosition = playerPosition,
                onPositionChange = {
                    coroutineScope.launch {
                        player.setPosition(it)
                    }
                },
            )
        }
    }
}
