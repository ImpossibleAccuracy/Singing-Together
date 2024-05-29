package com.singing.feature.record.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.paging.compose.LazyPagingItems
import com.singing.app.common.views.shared.player.PlayerView
import com.singing.app.common.views.toPlayerController
import com.singing.app.domain.features.RecordPlayer
import com.singing.app.domain.model.RecordData
import com.singing.app.domain.model.UserData
import com.singing.app.ui.screen.dimens
import com.singing.domain.model.RecordPoint


data class RecordDetailsData(
    val user: UserData?,
    val record: RecordData,
    val player: RecordPlayer,
    val editable: Boolean,
    val recordPoints: LazyPagingItems<RecordPoint>,
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
    actions: RecordDetailsActions,
) {
    Column(
        modifier = modifier.padding(vertical = MaterialTheme.dimens.dimen2),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1_5)
    ) {
        RecordDetailsMain(
            data = data,
            actions = actions,
        )

        Spacer(Modifier.height(MaterialTheme.dimens.dimen1_5))

        RecordDetailsPlayer(
            player = data.player,
            record = data.record,
        )

        RecordPointsView(
            modifier = Modifier.fillMaxWidth(),
            points = data.recordPoints,
            note = data.note,
        )
    }
}


@Composable
private fun RecordDetailsPlayer(
    modifier: Modifier = Modifier,
    player: RecordPlayer,
    record: RecordData,
) {
    PlayerView(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.secondary,
        inactiveTrackColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        playerController = player.toPlayerController(record)
    )
}
