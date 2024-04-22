package org.singing.app.di

import org.koin.core.module.Module
import org.koin.dsl.module
import org.singing.app.ui.screens.record.RecordScreenAudioViewModel

actual val additionalModule: Module = module {
    single {
        RecordScreenAudioViewModel()
    }
}
