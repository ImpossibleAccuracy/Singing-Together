package com.singing.app.di

import com.singing.app.data.setup.PlatformInitParams
import com.singing.app.data.setup.database.DatabaseParameters
import com.singing.app.data.setup.file.FileStoreProperties
import com.singing.app.data.setup.ktor.ApiParameters
import com.singing.app.di.module.authStoreModule
import com.singing.app.di.module.dataModule
import com.singing.app.di.module.useCaseModule
import com.singing.feature.account.profile.di.accountProfileModule
import com.singing.feature.auth.di.authModule
import com.singing.feature.community.di.communityModule
import com.singing.feature.main.di.mainModule
import com.singing.feature.publication.details.di.publicationDetailsModule
import com.singing.feature.record.di.recordDetailModule
import com.singing.feature.record.list.di.recordListModule
import com.singing.feature.recording.di.recordingModule
import com.singing.feature.recording.setup.recordingSetupModule
import org.koin.core.module.Module

fun totalAppModules(
    init: PlatformInitParams,
    apiParameters: ApiParameters,
    databaseParameters: DatabaseParameters,
    storeProperties: FileStoreProperties,
): List<Module> = listOf(
    dataModule(
        init = init,
        apiParameters = apiParameters,
        databaseParameters = databaseParameters,
        storeProperties = storeProperties
    ),
    authStoreModule,
    useCaseModule,

    mainModule,
    authModule,
    recordingModule,
    recordingSetupModule,
    recordListModule,
    recordDetailModule,
    communityModule,
    publicationDetailsModule,
    accountProfileModule,
)
