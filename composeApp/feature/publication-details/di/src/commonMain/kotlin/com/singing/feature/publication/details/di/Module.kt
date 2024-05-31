package com.singing.feature.publication.details.di

import com.singing.feature.main.PublicationDetailsViewModel
import org.koin.dsl.module

val publicationDetailsModule = module {
    factory { params ->
        PublicationDetailsViewModel(params.get(), get(), get(), get())
    }
}
