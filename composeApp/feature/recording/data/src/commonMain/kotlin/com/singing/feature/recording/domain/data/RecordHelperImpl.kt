package com.singing.feature.recording.domain.data

import com.singing.audio.capture.AudioCapture
import com.singing.feature.recording.domain.RecordHelper
import com.singing.feature.recording.domain.model.RecordResult
import com.singing.feature.recording.domain.model.RecordState
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RecordHelperImpl : RecordHelper {
    private val voiceCapture = AudioCapture()

    private val _recordResult = MutableStateFlow<RecordResult?>(null)
    override val recordResult: Flow<RecordResult?> = _recordResult.asStateFlow()

    private val _recordState = MutableStateFlow(RecordState.STOP)
    override val recordState: Flow<RecordState> = _recordState.asStateFlow()

    private val _countdown = MutableStateFlow<Int?>(null)
    override val countdown: Flow<Int?> = _countdown.asStateFlow()

    private var recordStart: Long = 0
    private var countdownJob: Job? = null

    override suspend fun startRecord() {
        _recordResult.value = null

        coroutineScope {
            async {
                startRecordCountdown()
            }.also {
                countdownJob = it
            }
        }.let {
            it.join()

            if (it.isCancelled) {
                return
            }
        }

        voiceCapture.capture()
        voiceCapture.awaitStart()
        recordStart = System.currentTimeMillis()

        _recordState.value = RecordState.RECORD
    }

    private suspend fun startRecordCountdown() {
        _recordState.value = RecordState.COUNTDOWN

        for (i in 3 downTo 1) {
            _countdown.value = i

            delay(1000)
        }

        _countdown.value = null
    }

    override fun stopRecordCountdown() {
        countdownJob?.cancel()

        _recordState.value = RecordState.STOP
        _countdown.value = null
    }

    override suspend fun stopRecord() {
        val result = voiceCapture.stop()

        _recordResult.value = RecordResult(
            bytes = result,
            duration = System.currentTimeMillis() - recordStart
        )

        _recordState.value = RecordState.STOP
    }
}