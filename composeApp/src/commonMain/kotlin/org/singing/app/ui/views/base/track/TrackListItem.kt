package org.singing.app.ui.views.base.track

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
import androidx.compose.ui.unit.dp
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.baseline_star_24
import com.singing.app.composeapp.generated.resources.baseline_star_border_24
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.ui.base.Space

@Composable
fun TrackListItem(
    modifier: Modifier = Modifier,
    filename: String,
    duration: String,
    isFavourite: Boolean,
    onFavouriteChange: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier.height(64.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
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
