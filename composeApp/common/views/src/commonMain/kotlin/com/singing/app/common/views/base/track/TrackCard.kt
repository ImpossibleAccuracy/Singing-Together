package com.singing.app.common.views.base.track

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.singing.app.common.views.model.state.TrackUiData
import com.singing.app.ui.utils.cardAppearance

@Composable
fun TrackCard(
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    data: TrackUiData,
    onFavouriteChange: (Boolean) -> Unit,
) {
    Box(
        modifier = modifier
            .cardAppearance(
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                shape = shape,
                background = MaterialTheme.colorScheme.surfaceContainerLow,
                padding = PaddingValues(
                    horizontal = 12.dp,
                    vertical = 8.dp,
                )
            )
    ) {
        TrackListItem(
            filename = data.filename,
            duration = data.duration,
            isFavourite = data.isFavourite,
            onFavouriteChange = onFavouriteChange,
        )
    }
}
