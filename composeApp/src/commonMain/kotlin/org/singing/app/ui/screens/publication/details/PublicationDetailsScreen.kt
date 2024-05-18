package org.singing.app.ui.screens.publication.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.singing.audio.player.PlayerState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import org.singing.app.di.module.viewModels
import org.singing.app.domain.model.Publication
import org.singing.app.domain.model.RecordPoint
import org.singing.app.setup.collectAsStateSafe
import org.singing.app.ui.base.Space
import org.singing.app.ui.common.ContentContainer
import org.singing.app.ui.common.player.RecordPlayer
import org.singing.app.ui.common.player.RecordPlayerScreen
import org.singing.app.ui.common.player.rememberRecordPlayer
import org.singing.app.ui.screens.record.details.views.RecordDetailsCard
import org.singing.app.ui.screens.record.details.views.RecordPointsView
import org.singing.app.ui.views.base.publication.PublicationCard
import org.singing.app.ui.views.base.publication.publicationCardAppearance
import org.singing.app.ui.views.shared.player.PlayerView

class PublicationDetailsScreen(
    private val requestedPublication: Publication,
) : RecordPlayerScreen() {
    @Composable
    override fun Content() {
        val viewModel = viewModels<PublicationDetailsViewModel>()
        val recordPlayer = rememberRecordPlayer()

        val verticalScroll = rememberScrollState()

        ContentContainer {
            Row(
                modifier = Modifier.verticalScroll(state = verticalScroll)
            ) {
                Column(
                    modifier = Modifier.weight(7f),
                ) {
                    RecordDetailsCard(
                        modifier = Modifier.fillMaxWidth(),
                        accountData = requestedPublication.author,
                        record = requestedPublication.record,
                        actions = null,
                    )

                    Space(16.dp)

                    RecordPointsViewContainer(
                        modifier = Modifier.fillMaxWidth(),
                        viewModel = viewModel,
                    )
                }

                Space(16.dp)

                Column(
                    modifier = Modifier.weight(3f),
                ) {
                    PublicationCard(
                        modifier = publicationCardAppearance(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        ),
                        publication = requestedPublication,
                        showActions = false,
                        navigatePublicationDetails = {}
                    )

                    Space(16.dp)

                    PlayerViewContainer(
                        modifier = Modifier.fillMaxWidth(),
                        recordPlayer = recordPlayer,
                    )
                }
            }
        }
    }

    @Composable
    private fun RecordPointsViewContainer(
        modifier: Modifier = Modifier,
        viewModel: PublicationDetailsViewModel,
    ) {
        var points by remember { mutableStateOf<ImmutableList<RecordPoint>>(persistentListOf()) }

        LaunchedEffect(requestedPublication) {
            points = viewModel
                .getRecordPoints(requestedPublication.record)
                .toImmutableList()
        }

        RecordPointsView(
            modifier = modifier,
            points = points,
            isTwoLineRecord = requestedPublication.record.isTwoLineRecord,
            note = viewModel::getNote
        )
    }

    @Composable
    private fun PlayerViewContainer(
        modifier: Modifier = Modifier,
        recordPlayer: RecordPlayer,
    ) {
        val coroutineScope = rememberCoroutineScope()

        val playerState by recordPlayer.state.collectAsStateSafe()
        val playerPosition by recordPlayer.position.collectAsStateSafe()

        PlayerView(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    top = 4.dp,
                    end = 16.dp,
                    bottom = 12.dp,
                ),
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.secondary,
            inactiveTrackColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            totalDuration = requestedPublication.record.duration,
            currentPosition = playerPosition,
            isPlaying = playerState == PlayerState.PLAY,
            onPositionChange = {
                coroutineScope.launch {
                    recordPlayer.setPosition(it)
                }
            },
            onPlay = {
                coroutineScope.launch {
                    recordPlayer.play(requestedPublication.record)
                }
            },
            onStop = {
                coroutineScope.launch {
                    recordPlayer.stop()
                }
            },
        )
    }
}
