package org.singing.app.ui.common.player

import androidx.compose.runtime.Stable
import kotlinx.coroutines.launch
import org.singing.app.ui.base.AppViewModel

@Stable
class RecordPlayerViewModel(
    val recordPlayer: RecordPlayer,
) : AppViewModel() {
    override fun onDispose() {
        resetRecordPlayer()

        super.onDispose()
    }

    fun resetRecordPlayer() {
        viewModelScope.launch {
            recordPlayer.reset()
        }
    }
}
