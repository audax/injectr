package net.daxbau.injectr

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

open class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startDi()
    }

    protected open fun startDi() {
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule)
        }
    }
}