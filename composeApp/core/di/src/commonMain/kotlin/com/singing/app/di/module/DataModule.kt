package com.singing.app.di.module

import com.singing.app.data.setup.PlatformInitParams
import com.singing.app.data.setup.database.DatabaseParameters
import com.singing.app.data.setup.database.setupAppDatabase
import com.singing.app.data.setup.file.FileStore
import com.singing.app.data.setup.file.FileStoreProperties
import com.singing.app.data.setup.ktor.setupKtorClient
import kotlinx.coroutines.runBlocking
import org.koin.dsl.module

fun dataModule(
    init: PlatformInitParams,
    databaseParameters: DatabaseParameters,
    storeProperties: FileStoreProperties,
) = module {
    includes(
        dataSourceModule,
        repositoryModule,
    )

    single {
        runBlocking { setupAppDatabase(init, databaseParameters) }
    }
    single { setupKtorClient() }
    single { FileStore(init, storeProperties) }
}
