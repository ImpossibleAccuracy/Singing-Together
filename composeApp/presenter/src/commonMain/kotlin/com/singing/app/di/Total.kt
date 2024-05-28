package com.singing.app.di

import com.singing.app.di.module.dataModule
import com.singing.app.di.module.useCaseModule
import com.singing.feature.community.di.communityModule
import com.singing.feature.main.di.mainModule
import com.singing.feature.record.di.recordDetailModule
import com.singing.feature.record.list.di.recordListModule
import org.koin.core.module.Module

fun totalAppModules(): List<Module> = mutableListOf<Module>().apply {
    add(useCaseModule)
    add(dataModule)

    add(mainModule)
    add(communityModule)
    add(recordListModule)
    add(recordDetailModule)
}
