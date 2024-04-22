package org.singing.app.ui.screens.record.views.display

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.singing.app.ComposeView
import org.singing.app.ui.helper.Space

data class AudioDisplayData(
    val icon: ImageVector,
    val title: String,
    val subtitle: String,
)

@Composable
fun AudioDisplay(
    modifier: Modifier = Modifier,
    firstData: AudioDisplayData,
    secondData: AudioDisplayData? = null,
    action: ComposeView,
) {
    val backgroundModifier =
        if (secondData == null) Modifier.background(MaterialTheme.colorScheme.primaryContainer)
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
                        imageVector = firstData.icon,
                        contentDescription = "microphone",
                        tint = MaterialTheme.colorScheme.primary,
                    )

                    Space(8.dp)

                    Text(
                        text = firstData.title,
                        color = MaterialTheme.colorScheme.primary,
                        style = TextStyle(
                            fontSize = 45.sp,
                            fontWeight = FontWeight.Black
                        )
                    )

                    Text(
                        text = firstData.subtitle,
                        color = MaterialTheme.colorScheme.primary,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Light
                        )
                    )
                }

                Space(12.dp)

                if (secondData != null) {
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
                            imageVector = secondData.icon,
                            contentDescription = "volume-high",
                            tint = MaterialTheme.colorScheme.tertiary,
                        )

                        Space(8.dp)

                        Text(
                            text = secondData.title,
                            color = MaterialTheme.colorScheme.tertiary,
                            style = TextStyle(
                                fontSize = 45.sp,
                                fontWeight = FontWeight.Black
                            )
                        )

                        Text(
                            text = secondData.subtitle,
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
