package org.singing.app.ui.screens.record.create.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.baseline_mic_24
import com.singing.app.composeapp.generated.resources.baseline_volume_up_black_24dp
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.ui.base.formatFrequency
import org.singing.app.ui.screens.record.create.viewmodel.model.RecordItem


@Composable
fun Display(
    firstInput: RecordItem?,
    secondInput: RecordItem?,
    recordCountdown: Int?,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(256.dp)
            .clip(shape = RoundedCornerShape(36.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .let {
                    if (recordCountdown == null) it
                    else it.blur(16.dp)
                }
        ) {
            val firstInputColor = MaterialTheme.colorScheme.primary
            val secondInputColor = MaterialTheme.colorScheme.secondary

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .let {
                        when (secondInput == null) {
                            true -> it.background(firstInputColor)

                            false -> it.background(
                                brush = Brush.linearGradient(
                                    0.4f to firstInputColor,
                                    0.6f to secondInputColor,
                                )
                            )
                        }
                    }
            ) {
                if (firstInput != null) {
                    InputLineDisplay(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(shape = MaterialTheme.shapes.extraLarge),
                        note = firstInput.note,
                        frequency = formatFrequency(firstInput.frequency),
                        icon = vectorResource(Res.drawable.baseline_mic_24),
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    )
                }

                if (secondInput != null) {
                    InputLineDisplay(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(shape = MaterialTheme.shapes.extraLarge),
                        note = secondInput.note,
                        frequency = formatFrequency(secondInput.frequency),
                        icon = vectorResource(Res.drawable.baseline_volume_up_black_24dp),
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                    )
                }
            }
        }

        if (recordCountdown != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme
                            .inverseSurface
                            .copy(
                                alpha = 0.4F
                            )
                    )
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = recordCountdown.toString(),
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    style = MaterialTheme.typography.displayMedium,
                )
            }
        }
    }
}
