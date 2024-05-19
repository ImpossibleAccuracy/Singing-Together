package org.singing.app.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.baseline_mic_24
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.vectorResource
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.createScope
import org.koin.core.scope.Scope
import org.singing.app.di.module.viewModels
import org.singing.app.domain.model.Publication
import org.singing.app.domain.model.RecordData
import org.singing.app.setup.collectAsStateSafe
import org.singing.app.ui.base.Space
import org.singing.app.ui.base.connectVerticalNestedScroll
import org.singing.app.ui.common.ContentContainer
import org.singing.app.ui.common.DefaultPagePaddings
import org.singing.app.ui.common.navigation.FabScreen
import org.singing.app.ui.common.player.rememberRecordPlayer
import org.singing.app.ui.screens.account.profile.AccountProfileScreen
import org.singing.app.ui.screens.main.views.*
import org.singing.app.ui.screens.publication.details.PublicationDetailsScreen
import org.singing.app.ui.screens.record.list.RecordListScreen
import org.singing.app.ui.screens.record.start.SelectRecordTypeScreen
import org.singing.app.ui.views.shared.record.RecordCardActionsCallbacks
import org.singing.app.ui.views.shared.record.dialog.DeleteRecordDialog
import org.singing.app.ui.views.shared.record.dialog.PublishRecordDialog
import org.singing.app.ui.views.shared.record.dialog.RecordPlayDialog
import kotlin.math.max

@Stable
class MainScreen : Screen, FabScreen, KoinScopeComponent {
    override val scope: Scope by lazy { createScope(this) }

    @Composable
    override fun Content() {
        val viewModel = viewModels<MainViewModel>(scope)

        LifecycleEffect {
            scope.close()
        }

        val verticalScroll = rememberScrollState()

        ContentContainer {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(
                        state = verticalScroll,
                    )
                    .padding(DefaultPagePaddings),
            ) {
                RecordBannerContainer(viewModel = viewModel)

                Space(36.dp)

                RecentRecordsContainer(
                    viewModel = viewModel,
                    gridModifier = Modifier.connectVerticalNestedScroll(1000.dp, verticalScroll),
                )

                Space(36.dp)

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val latestPublications by viewModel.latestPublications.collectAsStateSafe()

                    if (latestPublications.isNotEmpty()) {
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
                            RecentPublicationsListContainer(
                                viewModel = viewModel,
                                listModifier = Modifier.connectVerticalNestedScroll(600.dp, verticalScroll),
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    override fun Fab() {
        val viewModel = viewModels<MainViewModel>(scope)
        val navigator = LocalNavigator.currentOrThrow

        FloatingActionButton(
            elevation = FloatingActionButtonDefaults.elevation(0.dp),
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
            onClick = {
                navigator.push(
                    SelectRecordTypeScreen()
                )
            }
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.baseline_mic_24),
                contentDescription = "",
            )
        }
    }

    @Composable
    private fun RecordBannerContainer(
        viewModel: MainViewModel,
    ) {
        val navigator = LocalNavigator.currentOrThrow

        val user by viewModel.user.collectAsStateSafe()

        RecordBanner(
            user = user,
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
        val coroutineScope = rememberCoroutineScope()
        val navigator = LocalNavigator.currentOrThrow

        val user by viewModel.user.collectAsStateSafe()
        val records by viewModel.records.collectAsStateSafe()

        var showMainRecord by remember { mutableStateOf(true) }
        var recordToDelete by remember { mutableStateOf<RecordData?>(null) }
        var recordToPublish by remember { mutableStateOf<RecordData?>(null) }
        var recordToPlay by remember { mutableStateOf<RecordData?>(null) }

        if (recordToPlay != null) {
            val dialogRecordPlayer = rememberRecordPlayer()

            RecordPlayDialog(
                player = dialogRecordPlayer,
                record = recordToPlay!!,
                author = user,
                onDismiss = {
                    recordToPlay = null
                }
            )
        }

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
                    recordToPlay = it
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
                    coroutineScope.launch {
                        val publication = viewModel.getRecordPublication(it)

                        navigator.push(
                            PublicationDetailsScreen(
                                requestedPublication = publication,
                            )
                        )
                    }
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
    private fun RecentPublicationsListContainer(
        viewModel: MainViewModel,
        listModifier: Modifier = Modifier,
    ) {
        val navigator = LocalNavigator.currentOrThrow
        val mainRecordPlayer = rememberRecordPlayer()

        val latestPublications by viewModel.latestPublications.collectAsState()
        var publicationToPlay by remember { mutableStateOf<Publication?>(null) }

        if (publicationToPlay != null) {
            val dialogRecordPlayer = rememberRecordPlayer()

            RecordPlayDialog(
                player = dialogRecordPlayer,
                record = publicationToPlay!!.record,
                author = publicationToPlay!!.author,
                onDismiss = {
                    publicationToPlay = null
                }
            )
        }

        RecentPublicationsList(
            modifier = Modifier.fillMaxWidth(),
            listModifier = listModifier,
            publications = latestPublications,
            player = mainRecordPlayer,
            playPublication = {
                publicationToPlay = it
            },
            onAuthorClick = {
                navigator.push(
                    AccountProfileScreen(
                        requestedAccount = it.author,
                    )
                )
            },
            navigatePublicationDetails = {
                navigator.push(
                    PublicationDetailsScreen(
                        requestedPublication = it,
                    )
                )
            },
        )
    }
}
