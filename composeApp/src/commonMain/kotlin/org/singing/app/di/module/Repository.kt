package org.singing.app.di.module

import org.koin.dsl.module
import org.singing.app.domain.repository.RecordRepository

val repositoryModule = module {
    factory {
        RecordRepository(get())
    }
}
