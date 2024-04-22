package org.singing.app.setup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Composable
expect fun <T> StateFlow<T>.collectAsStateSafe(): State<T>

@Composable
expect fun <T> Flow<T>.collectAsStateSafe(default: T): State<T>
