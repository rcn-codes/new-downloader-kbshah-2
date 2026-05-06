package com.example.newdownloader26

import android.app.Application
import com.example.newdownloader26.core.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DownloaderApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@DownloaderApp)
            modules(appModule)
        }
    }
}
