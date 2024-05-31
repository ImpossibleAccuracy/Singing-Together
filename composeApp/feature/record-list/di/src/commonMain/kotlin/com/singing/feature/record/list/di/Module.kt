package com.singing.feature.record.list.di

import com.singing.feature.record.list.RecordListViewModel
import com.singing.feature.record.list.domain.usecase.GetAnyRecordUseCase
import com.singing.feature.record.list.domain.usecase.GetRecordListUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val recordListModule = module {
    factoryOf(::GetAnyRecordUseCase)
    factoryOf(::GetRecordListUseCase)

    factory { params ->
        RecordListViewModel(
            initialRecordData = params.getOrNull(),
            userProvider = get(),
            getRecordPointsUseCase = get(),
            getRecordListUseCase = get(),
            getAnyRecordUseCase = get(),
            findNoteUseCase = get(),
            uploadRecordUseCase = get(),
            publishRecordUseCase = get(),
            deleteRecordUseCase = get(),
            findRecordPublicationUseCase = get(),
            listenRecordUpdatesUseCase = get(),
        )
    }
}
