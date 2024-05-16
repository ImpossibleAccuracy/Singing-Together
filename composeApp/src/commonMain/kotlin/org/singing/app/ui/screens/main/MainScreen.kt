package org.singing.app.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.ScreenProvider
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.singing.app.di.module.viewModels
import org.singing.app.domain.model.RecordData
import org.singing.app.setup.collectAsStateSafe
import org.singing.app.ui.base.AppScreen
import org.singing.app.ui.base.Space
import org.singing.app.ui.base.connectVerticalNestedScroll
import org.singing.app.ui.screens.account.profile.AccountProfileScreen
import org.singing.app.ui.screens.main.views.*
import org.singing.app.ui.screens.record.create.SelectRecordTypeScreen
import org.singing.app.ui.screens.record.list.RecordListScreen
import org.singing.app.ui.views.shared.DeleteRecordDialog
import org.singing.app.ui.views.shared.PublishRecordDialog
import org.singing.app.ui.views.shared.RecordCardActionsCallbacks
import kotlin.math.max

class MainScreen : AppScreen(), ScreenProvider {
    @Composable
    override fun Content() {
        val viewModel = viewModels<MainViewModel>(true)

        val verticalScroll = rememberScrollState()

        ContentContainer {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(
                        state = verticalScroll,
                    )
                    .padding(
                        top = 16.dp,
                        bottom = 24.dp,
                    ),
            ) {
                RecordBannerContainer()

                Space(36.dp)

                RecentRecordsContainer(
                    viewModel = viewModel,
                    gridModifier = Modifier.connectVerticalNestedScroll(1000.dp, verticalScroll),
                )

                Space(36.dp)

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val minSize = 300
                    var size by remember { mutableStateOf(IntSize(0, 0)) }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = minSize.dp, max = max(minSize, size.height).dp)
                    ) {
                        RecentTracksContainer(
                            viewModel = viewModel,
                        )
                    }

                    Space(24.dp)

                    Box(
                        modifier = Modifier
                            .weight(2f)
                            .onGloballyPositioned {
                                size = it.size
                            }
                    ) {
                        PublicationsListContainer(
                            viewModel = viewModel,
                            listModifier = Modifier.connectVerticalNestedScroll(600.dp, verticalScroll),
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun RecordBannerContainer() {
        val navigator = LocalNavigator.currentOrThrow

        RecordBanner(
            onAction = {
                navigator.push(
                    SelectRecordTypeScreen()
                )
            }
        )
    }

    @Composable
    private fun RecentRecordsContainer(
        viewModel: MainViewModel,
        gridModifier: Modifier
    ) {
        val navigator = LocalNavigator.currentOrThrow

        val user by viewModel.user.collectAsStateSafe()
        val records by viewModel.records.collectAsStateSafe()

        var recordToDelete by remember { mutableStateOf<RecordData?>(null) }
        var recordToPublish by remember { mutableStateOf<RecordData?>(null) }

        var showMainRecord by remember { mutableStateOf(true) }

        RecentRecords(
            gridModifier = gridModifier,
            data = RecentRecordsData(
                user = user,
                records = records,
                showMainRecord = showMainRecord,
            ),
            actions = RecentRecordsActions(
                navigateAllRecords = {
                    navigator.push(
                        RecordListScreen()
                    )
                },
                navigateRecordDetails = {
                    navigator.push(
                        RecordListScreen(
                            defaultSelectedRecord = it,
                        )
                    )
                },
                onPlayRecord = {
                    TODO()
                },
                onDeleteRecord = {
                    recordToDelete = it
                }
            ),
            cardActions = RecordCardActionsCallbacks(
                onUploadRecord = {
                    viewModel.uploadRecord(it)
                },
                showPublication = {
                    TODO()
                },
                onPublishRecord = {
                    recordToPublish = it
                },
                onDeleteRecord = {
                    recordToDelete = it
                },
            )
        )

        if (recordToDelete != null) {
            DeleteRecordDialog(
                onConfirm = {
                    viewModel.deleteRecord(recordToDelete!!)

                    showMainRecord = false
                    recordToDelete = null
                },
                onDismiss = {
                    recordToDelete = null
                }
            )
        }

        if (recordToPublish != null) {
            PublishRecordDialog(
                onConfirm = {
                    viewModel.publishRecord(recordToPublish!!, it)

                    recordToPublish = null
                },
                onDismiss = {
                    recordToPublish = null
                },
            )
        }
    }

    @Composable
    private fun RecentTracksContainer(
        viewModel: MainViewModel,
    ) {
        val tracks by viewModel.recentTracks.collectAsStateSafe()

        RecentTracks(
            tracks = tracks,
            onFavouriteChange = { track, isFavourite ->
                viewModel.updateRecentTrackFavourite(track, isFavourite)
            }
        )
    }

    @Composable
    private fun PublicationsListContainer(
        viewModel: MainViewModel,
        listModifier: Modifier = Modifier
    ) {
        val navigator = LocalNavigator.currentOrThrow

        val latestPublications by viewModel.latestPublications.collectAsState()

        PublicationsList(
            modifier = Modifier.fillMaxWidth(),
            listModifier = listModifier,
            publications = latestPublications,
            onAuthorClick = {
                navigator.push(
                    AccountProfileScreen(
                        requestedAccount = it.author,
                    )
                )
            }
        )
    }
}
