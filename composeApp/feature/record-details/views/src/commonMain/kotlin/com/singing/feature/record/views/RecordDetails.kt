package com.singing.feature.record.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.LazyPagingItems
import com.singing.app.common.views.shared.record.RecordCardActionsState
import com.singing.app.domain.features.RecordPlayer
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.model.UserData
import com.singing.app.ui.screen.dimens
import com.singing.domain.model.RecordPoint


data class RecordDetailsData(
    val user: UserData?,
    val record: RecordData,
    val player: RecordPlayer?,
    val editable: Boolean,
    val recordPoints: LazyPagingItems<RecordPoint>,
    val isRecordPointsStatic: Boolean = false,
    val note: (Double) -> String,
)

data class RecordDetailsActions(
    val uploadRecord: () -> Unit,
    val navigatePublication: () -> Unit,
    val publishRecord: (String) -> Unit,
    val deleteRecord: () -> Unit,
)


@Composable
fun RecordDetails(
    modifier: Modifier = Modifier,
    data: RecordDetailsData,
    actions: RecordDetailsActions?,
    availableActions: RecordCardActionsState,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen2)
    ) {
        RecordDetailsMain(
            data = data,
            actions = actions,
            availableActions = availableActions,
        )

        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1_5)) {
            if (data.player != null) {
                RecordDetailsPlayer(
                    player = data.player,
                    record = data.record,
                )
            }

            RecordPointsView(
                modifier = Modifier.fillMaxWidth(),
                points = data.recordPoints,
                isLazyColumn = !data.isRecordPointsStatic,
                note = data.note,
            )
        }
    }
}
