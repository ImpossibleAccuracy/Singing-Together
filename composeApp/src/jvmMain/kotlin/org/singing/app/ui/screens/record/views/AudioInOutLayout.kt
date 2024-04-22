package org.singing.app.ui.screens.record.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.baseline_headphones_black_24dp
import com.singing.app.composeapp.generated.resources.baseline_mic_24
import com.singing.app.composeapp.generated.resources.baseline_volume_up_black_24dp
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.ui.helper.Space
import org.singing.app.ui.views.DropdownSelectChipActions
import org.singing.app.ui.views.DropdownSelectChipData
import org.singing.app.ui.views.DropdownSelectMenu

@Composable
fun AudioInOutLayout(
    modifier: Modifier = Modifier,
    data: AudioInOutLayoutData,
    actions: AudioInOutLayoutActions,
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        DropdownSelectMenu(
            modifier = Modifier.fillMaxWidth(),
            itemLabelText = { it.title },
            data = DropdownSelectChipData(
                items = data.voiceInputs,
            ),
            actions = DropdownSelectChipActions(
                onItemSelected = {
                    actions.onVoiceInputSelected(it!!)
                },
            ),
            icon = {
                Icon(
                    modifier = Modifier.size(18.dp),
                    imageVector = vectorResource(Res.drawable.baseline_mic_24),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary,
                )
            },
        )

        if (data.voiceOutputs != null) {
            Space(8.dp)

            DropdownSelectMenu(
                modifier = Modifier.fillMaxWidth(),
                itemLabelText = { it.title },
                data = DropdownSelectChipData(
                    items = data.voiceOutputs,
                    default = data.defaultNoChoiceLabel,
                ),
                actions = DropdownSelectChipActions(
                    onItemSelected = {
                        actions.onVoiceOutputSelected(it)
                    },
                ),
                icon = {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        imageVector = vectorResource(Res.drawable.baseline_headphones_black_24dp),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                },
            )
        }

        if (data.showAudioOutput && data.audioOutputs != null) {
            Space(8.dp)

            DropdownSelectMenu(
                modifier = Modifier.fillMaxWidth(),
                itemLabelText = { it.title },
                data = DropdownSelectChipData(
                    items = data.audioOutputs,
                    default = data.defaultNoChoiceLabel,
                ),
                actions = DropdownSelectChipActions(
                    onItemSelected = {
                        actions.onAudioOutputSelected(it)
                    },
                ),
                icon = {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        imageVector = vectorResource(Res.drawable.baseline_volume_up_black_24dp),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                },
            )
        }
    }
}
