package org.singing.app.ui.screens.record.views.filter

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.baseline_add_black_24dp
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.domain.model.audio.AudioFilter
import org.singing.app.ui.helper.Space
import org.singing.app.ui.views.AudioFilterItem

data class AudioInputFiltersData(
    val items: List<AudioFilter>
)

@Immutable
data class AudioInputFiltersActions(
    val onFiltersUpdate: (List<AudioFilter>) -> Unit,
)


@Composable
fun AudioInputFilters(
    modifier: Modifier = Modifier,
    data: AudioInputFiltersData,
    actions: AudioInputFiltersActions,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 12.dp
            )
    ) {
        Text(
            text = "Фильтры",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.titleSmall,
        )

        Space(12.dp)

        if (data.items.isEmpty()) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                text = "Добавте фильтры для аудио с микрофона",
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelLarge,
            )
        } else {
            Column {
                data.items.forEachIndexed { i, item ->
                    AudioFilterItem(
                        title = item.title,
                        onClose = {
                            val newList = ArrayList(data.items)

                            newList.remove(item)

                            actions.onFiltersUpdate(newList)
                        }
                    )

                    if (i < data.items.size - 1) {
                        Space(4.dp)
                    }
                }
            }
        }

        Space(12.dp)

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            onClick = {
                val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
                val title = (1..7)
                    .map { allowedChars.random() }
                    .joinToString("")

                actions.onFiltersUpdate(
                    data.items.plus(
                        AudioFilter(title)
                    )
                )
            }
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.baseline_add_black_24dp),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary,
            )

            Space(ButtonDefaults.IconSpacing)

            Text(
                text = "Фильтр",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}
