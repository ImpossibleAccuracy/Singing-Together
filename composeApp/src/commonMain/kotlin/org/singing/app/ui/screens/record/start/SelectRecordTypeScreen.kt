package org.singing.app.ui.screens.record.start

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.singing.app.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.singing.app.ui.base.Space
import org.singing.app.ui.screens.record.audio.SelectAudioScreen
import org.singing.app.ui.screens.record.create.RecordingScreen

class SelectRecordTypeScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primaryContainer),
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .widthIn(min = 700.dp)
                    .clip(shape = MaterialTheme.shapes.medium)
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(36.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(Res.string.title_select_record_type),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.displayMedium,
                    textAlign = TextAlign.Center,
                )

                Space(4.dp)

                Text(
                    text = stringResource(Res.string.subtitle_select_record_type),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                )

                Space(36.dp)

                Row {
                    SelectVariant(
                        title = stringResource(Res.string.choice_select_vocal_title),
                        subtitle = stringResource(Res.string.choice_select_vocal_subtitle).trimIndent(),
                        icon = vectorResource(Res.drawable.baseline_mic_24),
                        color = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        onClick = {
                            navigator.replace(RecordingScreen())
                        },
                    )

                    Space(24.dp)

                    SelectVariant(
                        title = stringResource(Res.string.choice_select_track_title),
                        subtitle = stringResource(Res.string.choice_select_track_subtitle).trimIndent(),
                        icon = vectorResource(Res.drawable.baseline_volume_up_black_24dp),
                        color = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                        onClick = {
                            navigator.push(
                                SelectAudioScreen()
                            )
                        },
                    )
                }
            }
        }
    }

    @Composable
    private fun SelectVariant(
        title: String,
        subtitle: String,
        icon: ImageVector,
        color: Color,
        contentColor: Color,
        onClick: () -> Unit,
    ) {
        Column(
            modifier = Modifier
                .width(300.dp)
                .heightIn(250.dp)
                .clip(shape = MaterialTheme.shapes.large)
                .background(color)
                .clickable(onClick = onClick),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = icon,
                tint = contentColor,
                contentDescription = "",
            )

            Space(8.dp)

            Text(
                text = title,
                color = contentColor,
                fontSize = 45.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
            )

            Text(
                text = subtitle,
                color = contentColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
            )
        }
    }

}
