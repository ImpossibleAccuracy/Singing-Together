package com.singing.feature.recording.setup

import com.singing.feature.recording.setup.usecase.GetFavouriteTracksUseCase
import com.singing.feature.recording.setup.usecase.GetTrackListUseCase
import com.singing.feature.recording.setup.usecase.ParseAudioUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val recordingSetupModule = module {
    factoryOf(::SelectAudioViewModel)
    factoryOf(::GetFavouriteTracksUseCase)
    factoryOf(::GetTrackListUseCase)
    factoryOf(::ParseAudioUseCase)
}
