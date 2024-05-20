package org.singing.app.ui.screens.record.create.save

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.action_back
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.stringResource
import org.singing.app.domain.model.RecordData
import com.singing.app.domain.model.RecordPoint
import org.singing.app.domain.repository.record.RecordSaveData
import org.singing.app.ui.base.cardAppearance
import org.singing.app.ui.screens.record.create.save.items.RecordInfoScreen
import org.singing.app.ui.views.base.AppTextButton

data class RecordSaveAdditionalInfo(
    val saveData: RecordSaveData,
    val duration: Long,
    val history: ImmutableList<RecordPoint>,
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RecordSaveDialog(
    data: RecordSaveAdditionalInfo,
    navigateToRecord: (RecordData) -> Unit,
    onDismiss: () -> Unit,
) {
    Navigator(
        screen = RecordInfoScreen(data),
        onBackPressed = { false }
    ) { navigator ->
        val currentScreen = navigator.lastItem as RecordSaveDialogScreen

        val isSkippable = currentScreen is SkippableRecordSaveDialogScreen
        val isFinal = currentScreen is RecordSavedScreen

        Dialog(
            onDismissRequest = {
                if (isSkippable) {
                    onDismiss()
                }
            },
        ) {
            Column(
                modifier = Modifier
                    .cardAppearance(
                        shape = RoundedCornerShape(28.dp),
                        background = MaterialTheme.colorScheme.surfaceContainerHigh,
                        padding = PaddingValues(24.dp)
                    ),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        text = "Record results",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 24.sp,
                    )

                    Text(
                        text = "Please check your record data before proceeding.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                Box(
                    modifier = Modifier.heightIn(max = 450.dp)
                ) {
                    SlideTransition(navigator = navigator) {
                        it.Content()
                    }
                }

                FlowRow(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    if (isSkippable && navigator.canPop) {
                        AppTextButton(
                            contentColor = MaterialTheme.colorScheme.primary,
                            label = stringResource(Res.string.action_back),
                            onClick = {
                                navigator.pop()
                            }
                        )
                    }

                    Spacer(Modifier.weight(1f))

                    AppTextButton(
                        enabled = isSkippable || isFinal,
                        contentColor = MaterialTheme.colorScheme.primary,
                        label = currentScreen.dismissText(),
                        onClick = onDismiss,
                    )

                    AppTextButton(
                        enabled = isSkippable || isFinal,
                        contentColor = MaterialTheme.colorScheme.primary,
                        label = currentScreen.confirmText(),
                        onClick = {
                            if (isFinal) {
                                val finalScreen = currentScreen as RecordSavedScreen

                                navigateToRecord(finalScreen.record)
                            } else {
                                val nextScreen = (currentScreen as SkippableRecordSaveDialogScreen)
                                    .buildNextPage()

                                navigator.push(nextScreen)
                            }
                        }
                    )
                }
            }
        }
    }
}
