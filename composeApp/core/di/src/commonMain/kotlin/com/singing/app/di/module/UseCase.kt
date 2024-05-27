package com.singing.app.di.module

import com.singing.app.domain.usecase.*
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val useCaseModule = module {
    factoryOf(::DeleteRecordUseCase)
    factoryOf(::PublishRecordUseCase)
    factoryOf(::UploadRecordUseCase)
    factoryOf(::GetRecordPointsUseCase)
    factoryOf(::FindRecordPublicationUseCase)
    factoryOf(::FindNoteUseCase)
}
