package com.singing.feature.publication.details.di

import com.singing.feature.publication.details.PublicationDetailsViewModel
import org.koin.dsl.module

val publicationDetailsModule = module {
    factory { params ->
        PublicationDetailsViewModel(
            initialPublication = params.get(),
            userProvider = get(),
            getRecordPointsUseCase = get(),
            findNoteUseCase = get(),
            deletePublicationUseCase = get()
        )
    }
}
