package com.singing.feature.record.di

import com.singing.feature.record.RecordDetailViewModel
import com.singing.feature.record.domain.usecase.ListenRecordUpdatesUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val recordDetailModule = module {
    factoryOf(::ListenRecordUpdatesUseCase)

    factory { params ->
        RecordDetailViewModel(
            initialRecordData = params.get(),
            listenRecordUpdatesUseCase = get(),
            findNoteUseCase = get(),
            getRecordPointsUseCase = get(),
            uploadRecordUseCase = get(),
            publishRecordUseCase = get(),
            deleteRecordUseCase = get(),
            findRecordPublicationUseCase = get(),
        )
    }
}
