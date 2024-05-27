package com.singing.app.common.views

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import com.singing.app.common.views.model.state.PlayerController
import com.singing.app.domain.features.RecordPlayer
import com.singing.app.domain.model.RecordData
import kotlinx.coroutines.launch

@Composable
fun RecordPlayer.toPlayerController(
    record: RecordData,
): PlayerController {
    val coroutineScope = rememberCoroutineScope()

    return PlayerController(
        isPlaying = this.isPlaying.collectAsState(false),
        playerPosition = this.position.collectAsState(),
        duration = record.duration,
        play = {
            coroutineScope.launch {
                play(record)
            }
        },
        stop = {
            coroutineScope.launch {
                stop()
            }
        },
        setPosition = { newPosition ->
            coroutineScope.launch {
                setPosition(newPosition)
            }
        },
    )
}
