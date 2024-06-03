package com.singing.feature.auth.di

import com.singing.feature.auth.AuthViewModel
import com.singing.feature.auth.domain.LoginUseCase
import com.singing.feature.auth.domain.RegistrationUseCase
import com.singing.feature.auth.domain.UserSearchUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val authModule = module {
    factoryOf(::AuthViewModel)
    factoryOf(::UserSearchUseCase)
    factoryOf(::LoginUseCase)
    factoryOf(::RegistrationUseCase)
}
