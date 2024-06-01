package com.singing.feature.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.movableContentWithReceiverOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.currentOrThrow
import com.singing.app.navigation.AppNavigator
import com.singing.app.navigation.SharedScreen
import com.singing.app.ui.screen.WindowSize
import com.singing.app.ui.screen.actualScreenSize
import com.singing.app.ui.screen.dimens
import com.singing.feature.main.viewmodel.MainIntent
import com.singing.feature.main.viewmodel.MainUiState
import com.singing.feature.main.views.RecentPublicationsListContainer
import com.singing.feature.main.views.RecentRecordsContainer
import com.singing.feature.main.views.RecentTracks
import com.singing.feature.main.views.RecordBanner
import kotlinx.coroutines.launch


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    uiState: MainUiState,
) {
    val navigator = AppNavigator.currentOrThrow
    val coroutineScope = rememberCoroutineScope()

    val verticalScroll = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(state = verticalScroll)
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen4_5),
    ) {
        RecordBanner(
            modifier = Modifier
                .fillMaxWidth()
                .height(256.dp),
            user = uiState.user,
            onAction = {
                navigator.navigate(SharedScreen.SelectRecordingType)
            }
        )

        Box(modifier = Modifier.heightIn(max = 1000.dp)) {
            RecentRecordsContainer(
                uiState = uiState,
                newIntent = {
                    viewModel.onIntent(it)
                },
                navigateToRecordPublication = {
                    coroutineScope.launch {
                        viewModel.getRecordPublication(it)?.let { publication ->
                            navigator.navigate(
                                SharedScreen.PublicationDetails(publication)
                            )
                        }
                    }
                },
                navigate = {
                    navigator.navigate(it)
                }
            )
        }

        if (uiState.recentTracks.isNotEmpty()) {
            val recentTracksContent = remember(uiState.recentTracks) {
                movableContentWithReceiverOf<Modifier> {
                    RecentTracks(
                        modifier = this.widthIn(min = 300.dp),
                        tracks = uiState.recentTracks,
                        onFavouriteChange = { track, isFavourite ->
                            viewModel.onIntent(
                                MainIntent.UpdateTrackFavourite(track, isFavourite)
                            )
                        }
                    )
                }
            }

            val recentPublications = remember(uiState.latestPublications) {
                movableContentWithReceiverOf<Modifier> {
                    RecentPublicationsListContainer(
                        modifier = this,
                        listModifier = Modifier,
                        latestPublications = uiState.latestPublications,
                        navigate = navigator::navigate,
                    )
                }
            }

            if (MaterialTheme.actualScreenSize <= WindowSize.MEDIUM) {
                recentTracksContent(Modifier.fillMaxWidth().heightIn(max = 1000.dp))

                if (uiState.user != null) {
                    recentPublications(Modifier.fillMaxWidth().heightIn(max = 1000.dp))
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 500.dp),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen3),
                ) {
                    recentTracksContent(Modifier.weight(1f))

                    if (uiState.user != null) {
                        recentPublications(Modifier.weight(2f))
                    }
                }
            }
        }
    }
}
