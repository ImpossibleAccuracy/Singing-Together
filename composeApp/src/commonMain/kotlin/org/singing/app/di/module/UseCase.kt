package org.singing.app.di.module

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import org.singing.app.domain.usecase.DeleteRecordUseCase
import org.singing.app.domain.usecase.PublishRecordUseCase
import org.singing.app.domain.usecase.UploadRecordUseCase

val useCaseModule = module {
    factoryOf(::DeleteRecordUseCase)
    factoryOf(::PublishRecordUseCase)
    factoryOf(::UploadRecordUseCase)
}
