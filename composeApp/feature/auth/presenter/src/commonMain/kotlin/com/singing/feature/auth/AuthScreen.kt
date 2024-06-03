package com.singing.feature.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.currentOrThrow
import com.singing.app.common.views.base.AppTextButton
import com.singing.app.common.views.base.account.AccountChip
import com.singing.app.common.views.base.list.Loader
import com.singing.app.navigation.AppNavigator
import com.singing.app.navigation.SharedScreen
import com.singing.app.ui.screen.dimens
import com.singing.app.ui.utils.cardAppearance
import com.singing.feature.auth.presenter.generated.resources.*
import com.singing.feature.auth.viewmodel.AuthIntent
import com.singing.feature.auth.viewmodel.AuthUiState
import com.singing.feature.auth.views.ConfirmButton
import com.singing.feature.auth.views.LoginField
import com.singing.feature.auth.views.PasswordField
import org.jetbrains.compose.resources.stringResource

private const val CONTENT_ANIMATION_DURATION = 200

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel,
    uiState: AuthUiState,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .widthIn(min = 350.dp, max = 550.dp)
                .heightIn(max = 650.dp)
                .verticalScroll(rememberScrollState())
                .cardAppearance(
                    shape = MaterialTheme.shapes.medium,
                    background = MaterialTheme.colorScheme.surfaceContainerHigh,
                    padding = PaddingValues(
                        horizontal = MaterialTheme.dimens.dimen1_5,
                        vertical = MaterialTheme.dimens.dimen3,
                    )
                )
        ) {
            AnimatedContent(
                targetState = uiState,
                contentKey = {
                    it::class.java
                },
                transitionSpec = {
                    slideInHorizontally(
                        animationSpec = tween(CONTENT_ANIMATION_DURATION),
                        initialOffsetX = { fullWidth -> fullWidth }
                    ) togetherWith slideOutHorizontally(
                        animationSpec = tween(CONTENT_ANIMATION_DURATION),
                        targetOffsetX = { fullWidth -> -fullWidth }
                    )
                }
            ) { targetState ->
                when (targetState) {
                    AuthUiState.Loading -> {
                        Loader()
                    }

                    is AuthUiState.EnterUsername -> {
                        EnterUsernameForm(
                            state = targetState,
                            newIntent = viewModel::onIntent,
                        )
                    }

                    is AuthUiState.PasswordConfirm -> {
                        PasswordConfirmForm(
                            state = targetState,
                            newIntent = viewModel::onIntent,
                        )
                    }

                    is AuthUiState.Registration -> {
                        RegistrationForm(
                            state = targetState,
                            newIntent = viewModel::onIntent,
                        )
                    }

                    is AuthUiState.Success -> {
                        AuthSuccess(state = targetState)
                    }
                }
            }
        }
    }
}

@Composable
private fun AuthSuccess(state: AuthUiState.Success) {
    val navigator = AppNavigator.currentOrThrow

    AuthFormContainer(
        title = stringResource(Res.string.title_auth_success),
        content = {
            Text(
                text = stringResource(Res.string.label_your_account),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium,
            )

            AccountChip(
                avatar = state.user.avatar,
                username = state.user.username,
            )
        },
        action = {
            ConfirmButton(
                label = stringResource(Res.string.action_back_to_app),
                onNext = {
                    navigator.backOrReplace(SharedScreen.Main)
                }
            )
        }
    )
}

@Composable
private fun EnterUsernameForm(
    state: AuthUiState.EnterUsername,
    newIntent: (AuthIntent) -> Unit,
) {
    AuthFormContainer(
        title = stringResource(Res.string.title_enter_login),
        content = {
            val focusRequester = remember { FocusRequester() }

            LoginField(
                value = state.username,
                onChange = {
                    newIntent(AuthIntent.UpdateLogin(it))
                },
                submit = {
                    newIntent(AuthIntent.SubmitLogin)
                },
                error = when {
                    state.isBlankError -> stringResource(Res.string.label_cannot_be_blank)
                    state.isTooLongError -> stringResource(Res.string.label_too_long)
                    else -> null
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
            )

            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        },
        action = {
            ConfirmButton(
                onNext = {
                    newIntent(AuthIntent.SubmitLogin)
                }
            )
        }
    )
}

@Composable
private fun PasswordConfirmForm(
    state: AuthUiState.PasswordConfirm,
    newIntent: (AuthIntent) -> Unit,
) {
    AuthFormContainer(
        title = stringResource(Res.string.title_welcome_back, state.user.username),
        content = {
            val focusRequester = remember { FocusRequester() }

            PasswordField(
                value = state.password,
                onChange = {
                    newIntent(AuthIntent.UpdatePassword(it))
                },
                submit = {
                    newIntent(AuthIntent.SubmitPassword)
                },
                error = when {
                    state.isBlankError -> stringResource(Res.string.label_cannot_be_blank)
                    state.isTooLongError -> stringResource(Res.string.label_too_long)
                    state.isPasswordMismatch -> stringResource(Res.string.label_password_mismatch)
                    else -> null
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
            )

            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        },
        secondaryAction = {
            AppTextButton(
                label = stringResource(Res.string.action_back),
                onClick = {
                    newIntent(AuthIntent.UpdateLogin(state.user.username))
                },
                contentColor = MaterialTheme.colorScheme.primary,
            )
        },
        action = {
            ConfirmButton(
                onNext = {
                    newIntent(AuthIntent.SubmitPassword)
                },
                label = stringResource(Res.string.action_sign_in),
            )
        }
    )
}

@Composable
private fun RegistrationForm(
    state: AuthUiState.Registration,
    newIntent: (AuthIntent) -> Unit,
) {
    AuthFormContainer(
        title = stringResource(Res.string.title_registration, state.username),
        subtitle = stringResource(Res.string.subtitle_registration),
        content = {
            val focusRequester = remember { FocusRequester() }

            PasswordField(
                value = state.password,
                onChange = {
                    newIntent(AuthIntent.UpdatePassword(it))
                },
                submit = {
                    newIntent(AuthIntent.SubmitPassword)
                },
                error = when {
                    state.isBlankError -> stringResource(Res.string.label_cannot_be_blank)
                    state.isTooLongError -> stringResource(Res.string.label_too_long)
                    else -> null
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
            )

            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        },
        secondaryAction = {
            AppTextButton(
                label = stringResource(Res.string.action_back),
                onClick = {
                    newIntent(AuthIntent.UpdateLogin(state.username))
                },
                contentColor = MaterialTheme.colorScheme.primary,
            )
        },
        action = {
            ConfirmButton(
                onNext = {
                    newIntent(AuthIntent.SubmitPassword)
                },
                label = stringResource(Res.string.action_sign_up),
            )
        }
    )
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AuthFormContainer(
    title: String,
    subtitle: String? = null,
    content: @Composable () -> Unit,
    secondaryAction: (@Composable () -> Unit)? = null,
    action: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = MaterialTheme.dimens.dimen4),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.dimen1),
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineLarge,
            maxLines = 1,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        if (subtitle != null) {
            Text(
                maxLines = 1,
                text = subtitle,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )

            Spacer(Modifier.height(0.dp))
        }

        content()

        FlowRow {
            if (secondaryAction != null) {
                secondaryAction()
            }

            Spacer(Modifier.weight(1f))

            action()
        }
    }
}
