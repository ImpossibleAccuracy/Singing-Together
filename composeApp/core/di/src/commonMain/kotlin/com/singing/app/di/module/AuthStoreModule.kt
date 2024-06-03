package com.singing.app.di.module

import com.singing.app.auth.AuthProviderImpl
import com.singing.app.domain.provider.UserProvider
import org.koin.dsl.module

val authStoreModule = module {
    single<UserProvider> { AuthProviderImpl() }
}
