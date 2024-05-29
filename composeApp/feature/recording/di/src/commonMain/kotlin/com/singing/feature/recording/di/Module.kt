package com.singing.feature.recording.di

import com.singing.feature.recording.RecordingViewModel
import com.singing.feature.recording.domain.ClockFlow
import com.singing.feature.recording.domain.InputListener
import com.singing.feature.recording.domain.PlayerHelper
import com.singing.feature.recording.domain.RecordHelper
import com.singing.feature.recording.domain.data.ClockFlowImpl
import com.singing.feature.recording.domain.data.InputListenerImpl
import com.singing.feature.recording.domain.data.PlayerHelperImpl
import com.singing.feature.recording.domain.data.RecordHelperImpl
import com.singing.feature.recording.save.RecordSaveViewModel
import org.koin.dsl.module

val recordingModule = module {
    factory {
        RecordingViewModel(
            findNoteUseCase = get(),
            userProvider = get(),
            inputListener = get(),
            playerHelper = get(),
            recordHelper = get(),
            clockFlow = get(),
        )
    }

    factory { RecordSaveViewModel(get()) }

    factory<ClockFlow> { ClockFlowImpl() }
    factory<InputListener> { InputListenerImpl() }
    factory<PlayerHelper> { PlayerHelperImpl() }
    factory<RecordHelper> { RecordHelperImpl() }
}
