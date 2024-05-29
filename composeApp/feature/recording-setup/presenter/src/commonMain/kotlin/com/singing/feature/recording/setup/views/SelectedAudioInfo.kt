package com.singing.feature.recording.setup.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.singing.app.common.views.base.AppFilledButton
import com.singing.app.domain.model.TrackParseResult
import com.singing.app.ui.screen.dimens
import com.singing.feature.recording.setup.presenter.generated.resources.*
import com.singing.feature.recording.setup.presenter.generated.resources.Res
import com.singing.feature.recording.setup.presenter.generated.resources.action_replace
import com.singing.feature.recording.setup.presenter.generated.resources.label_file_full_path
import com.singing.feature.recording.setup.presenter.generated.resources.label_file_name
import org.jetbrains.compose.resources.stringResource

@Composable
fun SelectedAudioInfo(
    audioProcessState: TrackParseResult,
    onResetState: () -> Unit,
    navigateNext: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        InfoItem(
            key = "${stringResource(Res.string.label_file_name)}:",
            value = audioProcessState.selectedAudio.name,
        )

        InfoItem(
            key = "${stringResource(Res.string.label_file_full_path)}:",
            value = audioProcessState.selectedAudio.file.fullPath,
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1_5, Alignment.End),
    ) {
        AppFilledButton(
            modifier = Modifier.widthIn(min = 180.dp),
            label = stringResource(Res.string.action_replace),
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            onClick = onResetState,
        )

        AppFilledButton(
            modifier = Modifier.widthIn(min = 180.dp),
            label = stringResource(Res.string.action_next),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            onClick = navigateNext,
        )
    }
}

@Composable
private fun InfoItem(
    key: String,
    value: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1)
    ) {
        Text(
            text = key,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelMedium,
            maxLines = 1,
        )

        Text(
            text = value,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.labelLarge,
            maxLines = 1,
        )
    }
}