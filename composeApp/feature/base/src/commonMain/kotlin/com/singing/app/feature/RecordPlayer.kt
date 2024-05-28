package com.singing.app.feature

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.lifecycle.LifecycleEffect
import cafe.adriel.voyager.navigator.currentOrThrow
import com.singing.app.domain.features.RecordPlayer
import com.singing.app.navigation.base.LocalAppPage
import kotlinx.coroutines.runBlocking
import org.koin.compose.getKoin


@Composable
fun rememberRecordPlayer(): RecordPlayer {
    // IDK, is it normally?
    val koin = getKoin()

    val recordPlayer = remember { koin.get<RecordPlayer>() }

    val appPage = LocalAppPage.currentOrThrow

    appPage.LifecycleEffect {
        runBlocking {
            recordPlayer.stop()
        }
    }

    return recordPlayer
}
