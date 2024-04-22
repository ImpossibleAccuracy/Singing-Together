package org.singing.app.setup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Composable
actual fun <T> StateFlow<T>.collectAsStateSafe(): State<T> =
    collectAsState()

@Composable
actual fun <T> Flow<T>.collectAsStateSafe(default: T): State<T> =
    collectAsState(default)
