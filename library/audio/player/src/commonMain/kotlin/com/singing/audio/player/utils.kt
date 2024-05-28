package com.singing.audio.player

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

suspend fun multiplyPlayers(
    player1: AudioPlayer,
    player2: AudioPlayer,
    play1: suspend (AudioPlayer) -> Flow<PlayerState>,
    play2: suspend (AudioPlayer) -> Flow<PlayerState>,
): Flow<PlayerState> = callbackFlow {
    launch {
        play2(player2)
            .collect { playerState ->
                if (playerState == PlayerState.STOP) {
                    player1.stop()
                }
            }
    }

    play1(player1)
        .collect { playerState ->
            if (playerState == PlayerState.STOP) {
                player2.stop()
            }

            send(playerState)
        }
}
