package org.singing.app.ui.screens.record.views.display

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.baseline_mic_24
import com.singing.app.composeapp.generated.resources.baseline_volume_up_black_24dp
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.ComposeView
import org.singing.app.ui.helper.Space
import org.singing.app.ui.screens.record.model.RecordPair
import kotlin.math.roundToInt

data class AudioDisplayData(
    val pair: RecordPair,
)

@Composable
fun AudioDisplay(
    modifier: Modifier = Modifier,
    data: AudioDisplayData,
    action: ComposeView,
) {
    with(data) {
        val backgroundModifier =
            if (pair.second == null) Modifier.background(MaterialTheme.colorScheme.primaryContainer)
            else Modifier.background(
                brush = Brush.linearGradient(
                    0.4f to MaterialTheme.colorScheme.primaryContainer,
                    0.6f to MaterialTheme.colorScheme.tertiaryContainer,
                )
            )

        Box(
            modifier = modifier
                .fillMaxSize()
                .then(backgroundModifier)
                .padding(vertical = 8.dp),
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    if (pair.first != null) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .padding(
                                    horizontal = 24.dp,
                                    vertical = 16.dp,
                                ),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Icon(
                                modifier = Modifier.size(size = 24.dp),
                                imageVector = vectorResource(Res.drawable.baseline_mic_24),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                            )

                            Space(8.dp)

                            Text(
                                text = pair.first!!.note,
                                color = MaterialTheme.colorScheme.primary,
                                style = TextStyle(
                                    fontSize = 45.sp,
                                    fontWeight = FontWeight.Black
                                )
                            )

                            Text(
                                text = "${pair.first!!.frequency.roundToInt()} Hz",
                                color = MaterialTheme.colorScheme.primary,
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Light
                                )
                            )
                        }
                    }

                    if (pair.first != null && pair.second != null) {
                        Space(12.dp)
                    }

                    if (pair.second != null) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .padding(
                                    horizontal = 24.dp,
                                    vertical = 16.dp,
                                ),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Icon(
                                modifier = Modifier.size(size = 24.dp),
                                imageVector = vectorResource(Res.drawable.baseline_volume_up_black_24dp),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary,
                            )

                            Space(8.dp)

                            Text(
                                text = pair.second!!.note,
                                color = MaterialTheme.colorScheme.tertiary,
                                style = TextStyle(
                                    fontSize = 45.sp,
                                    fontWeight = FontWeight.Black
                                )
                            )

                            Text(
                                text = "${pair.second!!.frequency.roundToInt()} Hz",
                                color = MaterialTheme.colorScheme.tertiary,
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Light
                                )
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    action()
                }
            }
        }
    }
}
