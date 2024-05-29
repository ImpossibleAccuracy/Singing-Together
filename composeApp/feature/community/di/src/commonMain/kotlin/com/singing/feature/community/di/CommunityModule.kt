package com.singing.feature.community.di

import com.singing.feature.community.CommunityViewModel
import com.singing.feature.community.domain.usecase.GetPopularPublicationTagsUseCase
import com.singing.feature.community.domain.usecase.GetRandomPublicationUseCase
import com.singing.feature.community.domain.usecase.SearchPublicationsUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val communityModule = module {
    factoryOf(::CommunityViewModel)
    factoryOf(::SearchPublicationsUseCase)
    factoryOf(::GetPopularPublicationTagsUseCase)
    factoryOf(::GetRandomPublicationUseCase)
}
