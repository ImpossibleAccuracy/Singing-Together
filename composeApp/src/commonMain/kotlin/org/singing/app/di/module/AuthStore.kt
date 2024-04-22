package org.singing.app.di.module

import org.koin.dsl.module
import org.singing.app.setup.auth.AuthStore

val authModule = module {
    single {
        AuthStore()
    }
}
