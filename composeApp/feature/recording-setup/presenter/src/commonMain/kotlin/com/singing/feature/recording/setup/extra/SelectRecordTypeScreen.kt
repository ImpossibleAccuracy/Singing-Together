package com.singing.feature.recording.setup.extra

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.currentOrThrow
import com.singing.app.navigation.AppNavigator
import com.singing.app.navigation.SharedScreen
import com.singing.app.ui.screen.dimens
import com.singing.feature.recording.setup.extra.views.SelectVariant
import com.singing.feature.recording.setup.presenter.generated.resources.Res
import com.singing.feature.recording.setup.presenter.generated.resources.baseline_mic_24
import com.singing.feature.recording.setup.presenter.generated.resources.baseline_volume_up_black_24dp
import com.singing.feature.recording.setup.presenter.generated.resources.choice_select_track_subtitle
import com.singing.feature.recording.setup.presenter.generated.resources.choice_select_track_title
import com.singing.feature.recording.setup.presenter.generated.resources.choice_select_vocal_subtitle
import com.singing.feature.recording.setup.presenter.generated.resources.choice_select_vocal_title
import com.singing.feature.recording.setup.presenter.generated.resources.subtitle_select_record_type
import com.singing.feature.recording.setup.presenter.generated.resources.title_select_record_type
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun SelectRecordTypeScreen(
    modifier: Modifier = Modifier,
) {
    val navigator = AppNavigator.currentOrThrow

    Box(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .widthIn(min = 700.dp)
                .clip(shape = MaterialTheme.shapes.medium)
                .background(color = MaterialTheme.colorScheme.background)
                .padding(36.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen4_5),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen0_5)) {
                Text(
                    text = stringResource(Res.string.title_select_record_type),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.displayMedium,
                    textAlign = TextAlign.Center,
                )

                Text(
                    text = stringResource(Res.string.subtitle_select_record_type),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen3)) {
                SelectVariant(
                    title = stringResource(Res.string.choice_select_vocal_title),
                    subtitle = stringResource(Res.string.choice_select_vocal_subtitle).trimIndent(),
                    icon = vectorResource(Res.drawable.baseline_mic_24),
                    color = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    onClick = {
                        navigator.replace(SharedScreen.Recording(null, true))
                    },
                )

                SelectVariant(
                    title = stringResource(Res.string.choice_select_track_title),
                    subtitle = stringResource(Res.string.choice_select_track_subtitle).trimIndent(),
                    icon = vectorResource(Res.drawable.baseline_volume_up_black_24dp),
                    color = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                    onClick = {
                        navigator.replace(SharedScreen.SelectRecordingAudio)
                    },
                )
            }
        }
    }
}
