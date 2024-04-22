package org.singing.app.di.module

import org.koin.dsl.module
import org.singing.app.setup.database.AppDatabase
import org.singing.app.setup.database.DatabaseDriverFactory

val databaseModule = module {
    factory {
        DatabaseDriverFactory()
    }

    single {
        AppDatabase(get())
    }
}
