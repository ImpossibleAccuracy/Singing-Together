package com.singing.app.di

import com.singing.app.di.module.dataModule
import com.singing.app.di.module.useCaseModule
import com.singing.feature.account.profile.di.accountProfileModule
import com.singing.feature.community.di.communityModule
import com.singing.feature.main.di.mainModule
import com.singing.feature.publication.details.di.publicationDetailsModule
import com.singing.feature.record.di.recordDetailModule
import com.singing.feature.record.list.di.recordListModule
import com.singing.feature.recording.di.recordingModule
import com.singing.feature.recording.setup.recordingSetupModule
import org.koin.core.module.Module

fun totalAppModules(): List<Module> = mutableListOf<Module>().apply {
    add(useCaseModule)
    add(dataModule)

    add(mainModule)
    add(recordingModule)
    add(recordingSetupModule)
    add(recordListModule)
    add(recordDetailModule)
    add(communityModule)
    add(publicationDetailsModule)
    add(accountProfileModule)
}
