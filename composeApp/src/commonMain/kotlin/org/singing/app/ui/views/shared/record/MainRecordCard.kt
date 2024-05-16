package org.singing.app.ui.views.shared.record

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.rememberImagePainter
import com.singing.app.composeapp.generated.resources.*
import nl.jacobras.humanreadable.HumanReadable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.domain.model.AccountUiData
import org.singing.app.domain.model.RecordData
import org.singing.app.ui.base.Space
import org.singing.app.ui.theme.extended
import org.singing.app.ui.views.base.AppFilledButton
import org.singing.app.ui.views.base.IconLabel
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainRecordCard(
    modifier: Modifier = Modifier,
    record: RecordData,
    creator: AccountUiData?,
    playRecord: () -> Unit,
    navigateRecordDetails: () -> Unit,
    cardActions: RecordCardActionsCallbacks,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape = MaterialTheme.shapes.medium)
            .background(color = MaterialTheme.colorScheme.secondaryContainer)
            .padding(all = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            FlowRow(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                RecordCardActions(
                    record = record,
                    actions = cardActions,
                )
            }

            Space(12.dp)

            Row(
                modifier = Modifier
                    .height(36.dp)
                    .clip(shape = RoundedCornerShape(50))
                    .background(color = MaterialTheme.colorScheme.surface),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    text = HumanReadable.timeAgo(record.createdAt),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelLarge,
                )

                if (record.isSavedRemote && creator != null) {
                    Image(
                        modifier = Modifier
                            .size(size = 36.dp)
                            .clip(shape = RoundedCornerShape(50)),
                        painter = when (creator.avatar) {
                            null -> painterResource(Res.drawable.baseline_person_24)
                            else -> rememberImagePainter(creator.avatar)
                        },
                        contentScale = ContentScale.Crop,
                        contentDescription = "Avatar",
                    )
                }
            }
        }

        Space(12.dp)

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .clip(shape = MaterialTheme.shapes.medium)
                    .background(color = MaterialTheme.extended.secondaryFixedDim)
                    .padding(
                        horizontal = 48.dp,
                        vertical = 24.dp
                    )
            ) {
                val text = when (record) {
                    is RecordData.Cover -> "${record.accuracy}% accuracy"
                    is RecordData.Vocal -> "N/A"
                }

                Text(
                    text = text,
                    color = MaterialTheme.extended.onSecondaryFixedVariant,
                    style = MaterialTheme.typography.titleLarge,
                )
            }

            Space(12.dp)

            Column {
                IconLabel(
                    leadingIcon = vectorResource(Res.drawable.baseline_folder_music_black_24dp),
                    label = when (record) {
                        is RecordData.Cover -> record.filename
                        is RecordData.Vocal -> "No track selected"
                    }
                )

                Space(4.dp)

                IconLabel(
                    leadingIcon = vectorResource(Res.drawable.baseline_access_time_24),
                    label = HumanReadable.duration(
                        record.duration.milliseconds
                            .inWholeSeconds.seconds
                    )
                )
            }
        }

        Space(12.dp)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = {
                    navigateRecordDetails()
                }
            ) {
                Text(
                    text = "See details",
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.labelLarge,
                )
            }

            Space(12.dp)

            AppFilledButton(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary,
                label = "Listen now",
                trailingIcon = vectorResource(Res.drawable.baseline_play_circle_filled_24),
                onClick = {
                    playRecord()
                }
            )
        }
    }
}