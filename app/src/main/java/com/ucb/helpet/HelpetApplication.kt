package com.ucb.helpet

import android.app.Application
import com.ucb.helpet.di.appModule
import com.ucb.helpet.di.remoteConfigModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class HelpetApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@HelpetApplication)
            modules(appModule, remoteConfigModule)
        }
    }
}
