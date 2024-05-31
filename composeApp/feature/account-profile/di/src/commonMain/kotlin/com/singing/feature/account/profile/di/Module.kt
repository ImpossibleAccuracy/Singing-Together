package com.singing.feature.account.profile.di

import com.singing.feature.account.profile.AccountProfileViewModel
import com.singing.feature.account.profile.domain.usecase.GetAccountPublicationsUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val accountProfileModule = module {
    factory { param ->
        AccountProfileViewModel(
            accountProfile = param.get(),
            getAccountPublicationsUseCase = get(),
            getUserInfoUseCase = get(),
        )
    }

    factoryOf(::GetAccountPublicationsUseCase)
}
