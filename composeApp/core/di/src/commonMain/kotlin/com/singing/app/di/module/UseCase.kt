package com.singing.app.di.module

import com.singing.app.domain.usecase.DeleteRecordUseCase
import com.singing.app.domain.usecase.FindNoteUseCase
import com.singing.app.domain.usecase.FindRecordPublicationUseCase
import com.singing.app.domain.usecase.GetRecordPointsUseCase
import com.singing.app.domain.usecase.ListenRecordUpdatesUseCase
import com.singing.app.domain.usecase.PublishRecordUseCase
import com.singing.app.domain.usecase.SearchPublicationUseCase
import com.singing.app.domain.usecase.UploadRecordUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val useCaseModule = module {
    factoryOf(::DeleteRecordUseCase)
    factoryOf(::FindNoteUseCase)
    factoryOf(::FindRecordPublicationUseCase)
    factoryOf(::GetRecordPointsUseCase)
    factoryOf(::ListenRecordUpdatesUseCase)
    factoryOf(::PublishRecordUseCase)
    factoryOf(::SearchPublicationUseCase)
    factoryOf(::UploadRecordUseCase)
}
