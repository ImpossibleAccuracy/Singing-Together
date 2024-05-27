package com.singing.app.di

import com.singing.app.di.module.dataModule
import com.singing.app.di.module.useCaseModule
import com.singing.feature.main.di.mainModule
import org.koin.core.module.Module

fun totalAppModules(): List<Module> = mutableListOf<Module>().apply {
    add(useCaseModule)
    add(dataModule)

    add(mainModule)
}
