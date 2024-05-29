package com.singing.feature.recording.save.items

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.singing.app.common.views.base.IconLabel
import com.singing.app.domain.model.RecordData
import com.singing.app.navigation.dialog.SkippableNavigationalDialogScreen
import com.singing.app.ui.screen.dimens
import com.singing.app.ui.theme.extended.extended
import com.singing.app.ui.utils.cardAppearance
import com.singing.feature.recording.presenter.generated.resources.*
import com.singing.feature.recording.save.RecordSaveAdditionalInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nl.jacobras.humanreadable.HumanReadable
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds


data class RecordInfoScreen(
    val data: RecordSaveAdditionalInfo
) : SkippableNavigationalDialogScreen<RecordData>() {
    override fun buildNextPage() =
        RecordPointsScreen(data)

    @Composable
    override fun Content() {
        val coroutineScope = rememberCoroutineScope()

        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen0_5),
        ) {
            if (data.saveData.track != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .cardAppearance(
                            shape = MaterialTheme.shapes.large,
                            background = MaterialTheme.extended.primaryFixedDim,
                            padding = PaddingValues(
                                horizontal = MaterialTheme.dimens.dimen6,
                                vertical = MaterialTheme.dimens.dimen3,
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

                Spacer(Modifier.height(MaterialTheme.dimens.dimen1))
            }

            IconLabel(
                leadingIcon = vectorResource(Res.drawable.baseline_folder_music_black_24dp),
                label = data.saveData.track?.name ?: stringResource(Res.string.label_no_track_selected),
            )

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
