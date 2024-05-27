package com.singing.app.common.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.core.screen.Screen
import com.singing.app.domain.features.RecordPlayer
import kotlinx.coroutines.runBlocking
import org.koin.compose.getKoin


@Composable
fun Screen.rememberRecordPlayer(): RecordPlayer {
    // IDK, is it normally?
    val koin = getKoin()

    val recordPlayer = remember { koin.get<RecordPlayer>() }

    LifecycleEffect {
        runBlocking {
            recordPlayer.stop()
        }
    }

    return recordPlayer
}
