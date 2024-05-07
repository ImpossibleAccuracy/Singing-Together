package org.singing.app.di

import org.koin.core.module.Module
import org.singing.app.di.module.databaseModule
import org.singing.app.di.module.networkModule
import org.singing.app.di.module.repositoryModule
import org.singing.app.di.module.viewModelsModule

val totalAppModules: List<Module> = listOf(
    viewModelsModule,
    databaseModule,
    networkModule,
    repositoryModule,
    additionalModule,
)
