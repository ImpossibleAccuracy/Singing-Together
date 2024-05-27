package com.singing.app.common.views.model.state

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State

@Immutable
data class PlayerController(
    val isPlaying: State<Boolean>,
    val playerPosition: State<Long>,
    val duration: Long,
    val play: () -> Unit,
    val stop: () -> Unit,
    val setPosition: (Long) -> Unit,
)