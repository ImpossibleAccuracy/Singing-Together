package org.singing.app.setup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Composable
actual fun <T> StateFlow<T>.collectAsStateSafe(): State<T> =
    collectAsStateWithLifecycle()

@Composable
actual fun <T> Flow<T>.collectAsStateSafe(default: T): State<T> =
    collectAsStateWithLifecycle(initialValue = default)
