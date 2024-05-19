package org.singing.app.ui.screens.record.details

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
import org.singing.app.di.module.viewModels
import org.singing.app.domain.model.AccountUiData
import org.singing.app.domain.model.RecordData
import org.singing.app.domain.model.RecordPoint
import org.singing.app.setup.collectAsStateSafe
import org.singing.app.ui.common.ContentContainer
import org.singing.app.ui.common.DefaultPagePaddings
import org.singing.app.ui.common.player.rememberRecordPlayer
import org.singing.app.ui.screens.record.details.views.RecordDetails
import org.singing.app.ui.screens.record.details.views.RecordDetailsActions
import org.singing.app.ui.screens.record.details.views.RecordDetailsData

class RecordDetailsScreen(
    private val requestedRecord: RecordData,
    private val author: AccountUiData,
) : Screen {
    @Composable
    override fun Content() {
        val viewModel = viewModels<RecordDetailsViewModel>()
        val recordPlayer = rememberRecordPlayer()
        val coroutineScope = rememberCoroutineScope()

        val record by viewModel.selectedRecord.collectAsStateSafe()

        val verticalScroll = rememberScrollState()

        LaunchedEffect(requestedRecord) {
            viewModel.selectedRecord.value = requestedRecord
        }

        ContentContainer {
            val localRecord = record

            if (localRecord != null) {
                var recordPoints by remember { mutableStateOf<ImmutableList<RecordPoint>>(persistentListOf()) }

                LaunchedEffect(localRecord) {
                    recordPoints = viewModel
                        .getRecordPoints(localRecord)
                        .toImmutableList()
                }

                RecordDetails(
                    modifier = Modifier
                        .fillMaxHeight()
                        .verticalScroll(
                            state = verticalScroll
                        )
                        .padding(DefaultPagePaddings),
                    data = RecordDetailsData(
                        record = localRecord,
                        user = author,
                        player = recordPlayer,
                        publication = {
                            coroutineScope.async {
                                viewModel.getRecordPublication(it)
                            }
                        },
                        recordPoints = recordPoints,
                        note = viewModel::getNote,
                    ),
                    actions = RecordDetailsActions(
                        uploadRecord = viewModel::uploadRecord,
                        publishRecord = viewModel::publishRecord,
                        deleteRecord = viewModel::deleteRecord,
                    )
                )
            }
        }
    }
}
