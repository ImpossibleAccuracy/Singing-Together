package org.singing.app.ui.views.track

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.baseline_star_24
import com.singing.app.composeapp.generated.resources.baseline_star_border_24
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.ui.base.Space

@Composable
fun TrackItem(
    modifier: Modifier = Modifier,
    filename: String,
    duration: String,
    isFavourite: Boolean,
    onFavouriteChange: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier,
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

        Space(8.dp)

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
