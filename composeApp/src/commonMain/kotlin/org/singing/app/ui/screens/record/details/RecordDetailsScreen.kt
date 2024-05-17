package org.singing.app.ui.screens.record.details

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.singing.app.di.module.viewModels
import org.singing.app.domain.model.AccountUiData
import org.singing.app.domain.model.RecordData
import org.singing.app.setup.collectAsStateSafe
import org.singing.app.ui.base.AppScreen
import org.singing.app.ui.screens.record.details.views.RecordDetails
import org.singing.app.ui.screens.record.details.views.RecordDetailsActions
import org.singing.app.ui.screens.record.details.views.RecordDetailsData

class RecordDetailsScreen(
    private val requestedRecord: RecordData,
    private val author: AccountUiData,
) : AppScreen() {
    private var _viewModel: RecordDetailsViewModel? = null

    override fun onLeave() {
        super.onLeave()

        _viewModel?.resetRecordPlayer()
    }

    @Composable
    override fun Content() {
        val viewModel = viewModels<RecordDetailsViewModel>()
        _viewModel = viewModel

        val record by viewModel.selectedRecord.collectAsStateSafe()

        val verticalScroll = rememberScrollState()

        LaunchedEffect(requestedRecord) {
            viewModel.selectedRecord.value = requestedRecord
        }

        ContentContainer {
            val localRecord = record

            if (localRecord != null) {
                RecordDetails(
                    modifier = Modifier
                        .fillMaxHeight()
                        .verticalScroll(
                            state = verticalScroll
                        ),
                    data = RecordDetailsData(
                        record = localRecord,
                        user = author,
                        player = viewModel.recordPlayer,
                        publication = viewModel::getRecordPublication,
                        recordPoints = viewModel::getRecordPoints,
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
