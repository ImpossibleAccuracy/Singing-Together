package org.singing.app.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import org.singing.app.ui.screens.main.views.*
import org.singing.app.ui.screens.record.create.SelectRecordTypeScreen
import org.singing.app.ui.screens.record.list.RecordListScreen
import org.singing.app.ui.screens.record.list.model.RecordOperationTab
import org.singing.app.ui.views.AppTextButton
import kotlin.math.max

class MainScreen : AppScreen(), ScreenProvider {
    private var _viewModel: MainViewModel? = null
    private val viewModel get() = _viewModel!!

    @Composable
    override fun Content() {
        _viewModel = viewModels()

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
                        RecentTracksContainer()
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
    private fun RecentRecordsContainer(gridModifier: Modifier) {
        val navigator = LocalNavigator.currentOrThrow

        val user by viewModel.user.collectAsStateSafe()
        val records by viewModel.records.collectAsStateSafe()

        var recordToDelete by remember { mutableStateOf<RecordData?>(null) }
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
                onShowMistakes = {
                    navigator.push(
                        RecordListScreen(
                            defaultSelectedRecord = it,
                            defaultSelectedTab = RecordOperationTab.Record
                        )
                    )
                },
                onDeleteRecord = {
                    recordToDelete = it
                }
            )
        )

        if (recordToDelete != null) {
            AlertDialog(
                title = {
                    Text("Delete record?")
                },
                text = {
                    Text("Record cannot be restored after delete.")
                },
                confirmButton = {
                    AppTextButton(
                        contentColor = MaterialTheme.colorScheme.primary,
                        label = "OK",
                        onClick = {
                            viewModel.deleteRecord(recordToDelete!!)

                            showMainRecord = false
                            recordToDelete = null
                        }
                    )
                },
                dismissButton = {
                    AppTextButton(
                        contentColor = MaterialTheme.colorScheme.primary,
                        label = "Cancel",
                        onClick = {
                            recordToDelete = null
                        }
                    )
                },
                onDismissRequest = {
                    recordToDelete = null
                }
            )
        }
    }

    @Composable
    private fun RecentTracksContainer() {
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
        listModifier: Modifier = Modifier
    ) {
        val latestPublications by viewModel.latestPublications.collectAsState()

        PublicationsList(
            modifier = Modifier.fillMaxWidth(),
            listModifier = listModifier,
            publications = latestPublications,
        )
    }
}
