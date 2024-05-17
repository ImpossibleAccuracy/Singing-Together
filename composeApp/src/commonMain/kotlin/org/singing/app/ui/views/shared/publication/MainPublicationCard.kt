package org.singing.app.ui.views.shared.publication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.rememberImagePainter
import com.singing.app.composeapp.generated.resources.*
import com.singing.audio.player.PlayerState
import kotlinx.coroutines.launch
import nl.jacobras.humanreadable.HumanReadable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.domain.model.Publication
import org.singing.app.domain.model.RecordData
import org.singing.app.domain.player.RecordPlayer
import org.singing.app.setup.collectAsStateSafe
import org.singing.app.ui.base.Space
import org.singing.app.ui.views.base.progress.TimeProgress
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds


private val shape
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.shapes.medium

val chipShape
    @Composable
    @ReadOnlyComposable
    get() = MaterialTheme.shapes.medium

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainPublicationCard(
    modifier: Modifier = Modifier,
    publication: Publication,
    player: RecordPlayer,
) {
    val coroutineScope = rememberCoroutineScope()

    val playerState by player.state.collectAsStateSafe()
    val playerPosition by player.position.collectAsStateSafe()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, color = MaterialTheme.colorScheme.outlineVariant, shape = shape)
            .clip(shape = shape)
            .background(color = MaterialTheme.colorScheme.surfaceContainerLow)
            .padding(all = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = Modifier
                    .clip(shape = MaterialTheme.shapes.medium)
                    .background(color = MaterialTheme.colorScheme.surface)
                    .padding(
                        start = 16.dp,
                        top = 8.dp,
                        end = 24.dp,
                        bottom = 8.dp,
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(
                    modifier = Modifier
                        .size(size = 36.dp)
                        .clip(shape = RoundedCornerShape(50)),
                    painter = when (publication.author.avatar) {
                        null -> painterResource(Res.drawable.baseline_person_24)
                        else -> rememberImagePainter(publication.author.avatar)
                    },
                    contentScale = ContentScale.Crop,
                    contentDescription = "Avatar",
                )

                Space(12.dp)

                Column {
                    Text(
                        text = publication.author.username,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium,
                    )

                    Text(
                        text = HumanReadable.timeAgo(publication.createdAt),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            IconButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.baseline_open_in_new_24),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = "",
                )
            }
        }

        Space(8.dp)

        Text(
            text = publication.description,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium,
        )

        Space(16.dp)

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {

            Box(
                modifier = Modifier
                    .height(36.dp)
                    .border(1.dp, color = MaterialTheme.colorScheme.outlineVariant, shape = chipShape)
                    .clip(shape = chipShape)
                    .background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = when (publication.record) {
                        is RecordData.Cover -> "${publication.record.accuracy}% accuracy"
                        is RecordData.Vocal -> "N/A"
                    },
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            Row(
                modifier = Modifier
                    .height(36.dp)
                    .border(1.dp, color = MaterialTheme.colorScheme.outlineVariant, shape = chipShape)
                    .clip(shape = chipShape)
                    .background(color = MaterialTheme.colorScheme.surfaceContainerLowest)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.baseline_access_time_24),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = "",
                )

                Space(8.dp)

                Text(
                    text = HumanReadable.duration(
                        publication.record.duration.milliseconds
                            .inWholeSeconds.seconds
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }

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
                    tint = MaterialTheme.colorScheme.secondary,
                    contentDescription = "",
                )
            }

            Space(4.dp)

            TimeProgress(
                modifier = Modifier.weight(1f),
                contentColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
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