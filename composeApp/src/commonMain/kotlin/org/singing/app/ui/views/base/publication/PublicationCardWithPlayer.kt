package org.singing.app.ui.views.base.publication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        slotAfterAuthor = {
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
        },
        slotAfterDescription = {
            Divider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
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
        },
    )
}
