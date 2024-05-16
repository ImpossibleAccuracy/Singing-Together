package org.singing.app.di.module

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.singing.app.domain.usecase.*

val useCaseModule = module {
    factoryOf(::DeleteRecordUseCase)
    factoryOf(::PublishRecordUseCase)
    factoryOf(::UploadRecordUseCase)
    factoryOf(::GetRecordPointsUseCase)
    factoryOf(::FindRecordPublicationUseCase)
    factoryOf(::FindNoteUseCase)
}
