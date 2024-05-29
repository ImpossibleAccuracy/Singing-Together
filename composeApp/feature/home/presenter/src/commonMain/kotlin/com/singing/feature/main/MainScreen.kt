package com.singing.feature.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.currentOrThrow
import com.singing.app.navigation.AppNavigator
import com.singing.app.navigation.SharedScreen
import com.singing.app.ui.screen.dimens
import com.singing.feature.main.viewmodel.MainIntent
import com.singing.feature.main.viewmodel.MainUiState
import com.singing.feature.main.views.RecentPublicationsListContainer
import com.singing.feature.main.views.RecentRecordsContainer
import com.singing.feature.main.views.RecentTracks
import com.singing.feature.main.views.RecordBanner
import kotlinx.coroutines.launch


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
        modifier = modifier.verticalScroll(state = verticalScroll),
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

        if (uiState.latestPublications.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 500.dp),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen3),
            ) {
                RecentTracks(
                    modifier = Modifier.weight(1f),
                    tracks = uiState.recentTracks,
                    onFavouriteChange = { track, isFavourite ->
                        viewModel.onIntent(
                            MainIntent.UpdateTrackFavourite(track, isFavourite)
                        )
                    }
                )

                Box(modifier = Modifier.weight(2f)) {
                    RecentPublicationsListContainer(
                        uiState = uiState,
                        navigate = {
                            navigator.navigate(it)
                        }
                    )
                }
            }
        }
    }
}
