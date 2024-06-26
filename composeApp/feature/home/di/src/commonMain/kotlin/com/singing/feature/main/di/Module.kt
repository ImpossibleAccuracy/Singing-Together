package com.singing.feature.main.di

import com.singing.feature.main.MainViewModel
import com.singing.feature.main.domain.usecase.GetRecentPublicationsUseCase
import com.singing.feature.main.domain.usecase.GetRecentRecordListUseCase
import com.singing.feature.main.domain.usecase.GetRecentTracksUseCase
import com.singing.feature.main.domain.usecase.UpdateTrackFavouriteUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val mainModule = module {
    factoryOf(::GetRecentPublicationsUseCase)
    factoryOf(::GetRecentRecordListUseCase)
    factoryOf(::GetRecentTracksUseCase)
    factoryOf(::UpdateTrackFavouriteUseCase)

    factoryOf(::MainViewModel)
}
