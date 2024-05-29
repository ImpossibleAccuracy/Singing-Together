package com.singing.app.navigation.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.singing.app.common.views.base.AppTextButton
import com.singing.app.ui.screen.dimens
import com.singing.app.ui.utils.cardAppearance


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun <T> NavigationalDialog(
    startScreen: () -> NavigationalDialogScreen<T>,
    backButtonText: @Composable (NavigationalDialogScreen<T>) -> String,
    dismissButtonText: @Composable (NavigationalDialogScreen<T>) -> String,
    confirmButtonText: @Composable (NavigationalDialogScreen<T>) -> String,
    onDismiss: () -> Unit,
    onFinish: (T) -> Unit,
    content: @Composable (@Composable () -> Unit) -> Unit,
) {
    Navigator(
        screen = startScreen(),
        onBackPressed = { false }
    ) { navigator ->
        @Suppress("UNCHECKED_CAST")
        val currentScreen = navigator.lastItem as NavigationalDialogScreen<T>

        LaunchedEffect(currentScreen) {
            currentScreen.navigator = navigator
        }

        val isSkippable = currentScreen is SkippableNavigationalDialogScreen
        val isFinal = currentScreen is FinalNavigationalDialogScreen

        Dialog(
            onDismissRequest = {
                if (isSkippable || isFinal) {
                    onDismiss()
                }
            },
        ) {
            Column(
                modifier = Modifier.cardAppearance(
                    shape = RoundedCornerShape(28.dp),
                    background = MaterialTheme.colorScheme.surfaceContainerHigh,
                    padding = PaddingValues(MaterialTheme.dimens.dimen3),
                ),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                content {
                    SlideTransition(navigator = navigator) {
                        it.Content()
                    }
                }

                FlowRow(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1),
                ) {
                    if (isSkippable && navigator.canPop) {
                        AppTextButton(
                            contentColor = MaterialTheme.colorScheme.primary,
                            label = backButtonText(currentScreen),
                            onClick = {
                                navigator.pop()
                            }
                        )
                    }

                    Spacer(Modifier.weight(1f))

                    AppTextButton(
                        enabled = isSkippable || isFinal,
                        contentColor = MaterialTheme.colorScheme.primary,
                        label = dismissButtonText(currentScreen),
                        onClick = onDismiss,
                    )

                    AppTextButton(
                        enabled = isSkippable || isFinal,
                        contentColor = MaterialTheme.colorScheme.primary,
                        label = confirmButtonText(currentScreen),
                        onClick = {
                            if (isFinal) {
                                val finalScreen = currentScreen as FinalNavigationalDialogScreen
                                val result = finalScreen.result

                                onFinish(result)
                            } else {
                                val nextScreen = (currentScreen as SkippableNavigationalDialogScreen)
                                    .buildNextPage()

                                navigator.popUntilRoot()
                                navigator.replace(nextScreen)
                            }
                        }
                    )
                }
            }
        }
    }
}
