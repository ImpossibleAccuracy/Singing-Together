package com.singing.app

import android.app.Application
import com.singing.app.data.setup.PlatformInitParams
import com.singing.app.data.setup.database.DatabaseParameters
import com.singing.app.data.setup.file.FileStoreProperties
import com.singing.app.di.totalAppModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MyApplication)

            modules(
                totalAppModules(
                    init = PlatformInitParams(applicationContext),
                    databaseParameters = DatabaseParameters("AppDatabase.dp"),
                    storeProperties = FileStoreProperties(),
                )
            )
        }
    }
}
