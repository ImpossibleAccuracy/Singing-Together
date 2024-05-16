package org.singing.app.di

import org.koin.core.module.Module
import org.singing.app.di.module.*

val totalAppModules: List<Module> = listOf(
    viewModelsModule,
    useCaseModule,
    repositoryModule,
    networkModule,
    databaseModule,
    additionalModule,
)
