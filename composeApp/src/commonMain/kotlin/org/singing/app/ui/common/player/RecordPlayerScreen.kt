package org.singing.app.ui.common.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import org.singing.app.di.module.viewModels
import org.singing.app.ui.base.AppScreen

@Stable
abstract class RecordPlayerScreen : AppScreen() {
    var playerViewModel: RecordPlayerViewModel? = null
        internal set

    override fun onLeave() {
        super.onLeave()

        playerViewModel?.resetRecordPlayer()
    }

    override fun onClose() {
        super.onClose()

        playerViewModel?.resetRecordPlayer()
    }
}

@Composable
fun RecordPlayerScreen.rememberRecordPlayer(): RecordPlayer {
    val viewModel = viewModels<RecordPlayerViewModel>().also {
        playerViewModel = it
    }

    return remember { viewModel.recordPlayer }
}
