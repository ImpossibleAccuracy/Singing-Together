package org.singing.app

import android.app.Application
import android.content.Context
import com.singing.audio.utils.backgroundScope
import kotlinx.coroutines.cancel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.singing.app.di.totalAppModules

class MyApplication : Application() {
    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()

        appContext = applicationContext

        startKoin {
            androidLogger()
            androidContext(this@MyApplication)

            modules(totalAppModules)
        }
    }

    override fun onTerminate() {
        super.onTerminate()

        backgroundScope.cancel()
    }
}
