package com.singing.feature.auth.views

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.singing.app.common.views.base.AppFilledButton
import com.singing.feature.auth.presenter.generated.resources.Res
import com.singing.feature.auth.presenter.generated.resources.action_next
import org.jetbrains.compose.resources.stringResource

@Composable
fun ConfirmButton(
    modifier: Modifier = Modifier,
    label: String = stringResource(Res.string.action_next),
    onNext: () -> Unit
) {
    AppFilledButton(
        modifier = modifier,
        label = label,
        onClick = onNext,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    )
}