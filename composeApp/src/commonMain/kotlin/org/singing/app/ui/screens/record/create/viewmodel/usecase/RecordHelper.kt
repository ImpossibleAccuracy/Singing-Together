package org.singing.app.ui.screens.record.create.viewmodel.usecase

import com.singing.audio.capture.AudioCapture
import kotlinx.coroutines.*
import org.singing.app.ui.screens.record.create.viewmodel.model.RecordState

class RecordHelper(
    private val coroutineScope: CoroutineScope,
    private val onRecordStateUpdate: (RecordState) -> Unit,
    private val onNewCountdownValue: (Int?) -> Unit,
    private val onBeforeRecord: () -> Unit,
    private val onRecordDone: (record: ByteArray) -> Unit,
) {
    private val voiceCapture = AudioCapture()

    private var countdownJob: Job? = null

    fun startRecord() {
        coroutineScope.launch {
            startRecordCountdown().let {
                it.join()

                if (it.isCancelled) {
                    return@launch
                }
            }

            onBeforeRecord()

            voiceCapture.capture()
            voiceCapture.awaitStart()

            onRecordStateUpdate(RecordState.RECORD)
        }
    }

    private fun startRecordCountdown() =
        coroutineScope.async {
            onRecordStateUpdate(RecordState.COUNTDOWN)

            for (i in 3 downTo 1) {
                onNewCountdownValue(i)
                delay(1000)
            }

            onNewCountdownValue(null)
        }.also {
            countdownJob = it
        }

    fun stopRecordCountdown() {
        countdownJob?.cancel()

        onRecordStateUpdate(RecordState.STOP)
        onNewCountdownValue(null)
    }

    fun stopRecord() {
        onRecordStateUpdate(RecordState.STOP)

        coroutineScope.launch {
            val result = voiceCapture.stop()

            onRecordDone(result)
        }
    }
}
