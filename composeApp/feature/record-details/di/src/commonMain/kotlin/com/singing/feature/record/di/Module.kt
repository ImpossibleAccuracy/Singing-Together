package com.singing.feature.record.di

import com.singing.feature.record.RecordDetailViewModel
import org.koin.dsl.module

val recordDetailModule = module {
    factory { params ->
        RecordDetailViewModel(
            initialRecordData = params.get(),
            userProvider = get(),
            getRecordPointsUseCase = get(),
            listenRecordUpdatesUseCase = get(),
            findNoteUseCase = get(),
            uploadRecordUseCase = get(),
            publishRecordUseCase = get(),
            deleteRecordUseCase = get(),
            findRecordPublicationUseCase = get(),
        )
    }
}
