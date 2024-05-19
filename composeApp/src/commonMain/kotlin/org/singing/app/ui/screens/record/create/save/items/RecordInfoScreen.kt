package org.singing.app.ui.screens.record.create.save.items

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.singing.app.composeapp.generated.resources.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nl.jacobras.humanreadable.HumanReadable
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.ui.base.Space
import org.singing.app.ui.base.cardAppearance
import org.singing.app.ui.screens.record.create.save.RecordSaveAdditionalInfo
import org.singing.app.ui.screens.record.create.save.SkippableRecordSaveDialogScreen
import org.singing.app.ui.theme.extended
import org.singing.app.ui.views.base.IconLabel
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds


data class RecordInfoScreen(
    val data: RecordSaveAdditionalInfo,
) : SkippableRecordSaveDialogScreen() {
    override fun buildNextPage() =
        RecordPointsScreen(data)

    @Composable
    override fun Content() {
        val coroutineScope = rememberCoroutineScope()

        Column {
            if (data.saveData.track != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .cardAppearance(
                            shape = MaterialTheme.shapes.large,
                            background = MaterialTheme.extended.primaryFixedDim,
                            padding = PaddingValues(
                                horizontal = 48.dp,
                                vertical = 24.dp,
                            )
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    var accuracy by remember { mutableStateOf<Int?>(null) }

                    LaunchedEffect(Unit) {
                        coroutineScope.launch(Dispatchers.Default) {
                            accuracy = data.history.sumOf { it.accuracy.percent.toInt() } / data.history.size
                        }
                    }

                    if (accuracy == null) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    } else {
                        Text(
                            text = stringResource(
                                Res.string.label_accuracy,
                                accuracy!!
                            ),
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                }

                Space(12.dp)
            }

            IconLabel(
                leadingIcon = vectorResource(Res.drawable.baseline_folder_music_black_24dp),
                label = data.saveData.track?.name ?: stringResource(Res.string.label_no_track_selected),
            )

            Space(4.dp)

            IconLabel(
                leadingIcon = vectorResource(Res.drawable.baseline_access_time_24),
                label = HumanReadable.duration(
                    data.duration.milliseconds
                        .inWholeSeconds.seconds
                ),
            )
        }
    }
}
