package com.singing.feature.auth.views

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import com.singing.feature.auth.presenter.generated.resources.Res
import com.singing.feature.auth.presenter.generated.resources.label_enter_login
import com.singing.feature.auth.presenter.generated.resources.label_login
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginField(
    modifier: Modifier = Modifier,
    value: String,
    error: String?,
    onChange: (String) -> Unit,
    submit: () -> Unit,
    icon: ImageVector = Icons.Default.Person,
    label: String = stringResource(Res.string.label_login),
    placeholder: String = stringResource(Res.string.label_enter_login),
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        modifier = modifier,
        isError = error != null,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(
            onDone = { submit() }
        ),
        placeholder = { Text(placeholder) },
        label = { Text(label) },
        supportingText = error?.let { { Text(error) } },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onSurface,
            )
        },
        singleLine = true,
        visualTransformation = VisualTransformation.None
    )
}