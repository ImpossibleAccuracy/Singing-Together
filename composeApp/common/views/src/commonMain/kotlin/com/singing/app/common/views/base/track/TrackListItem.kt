package com.singing.app.common.views.base.track

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.singing.app.common.views.views.generated.resources.Res
import com.singing.app.common.views.views.generated.resources.baseline_star_24
import com.singing.app.common.views.views.generated.resources.baseline_star_border_24
import com.singing.app.ui.screen.dimens
import org.jetbrains.compose.resources.vectorResource


@Composable
fun TrackListItem(
    modifier: Modifier = Modifier,
    filename: String,
    duration: String,
    isFavourite: Boolean,
    onFavouriteChange: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier.height(MaterialTheme.dimens.dimen4 * 2),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1),
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = filename,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
            )

            Text(
                text = duration,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
            )
        }

        IconButton(
            onClick = {
                onFavouriteChange(!isFavourite)
            }
        ) {
            Icon(
                imageVector = vectorResource(
                    if (isFavourite) Res.drawable.baseline_star_24
                    else Res.drawable.baseline_star_border_24
                ),
                tint = MaterialTheme.colorScheme.tertiary,
                contentDescription = "",
            )
        }
    }
}
