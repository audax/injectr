package net.daxbau.injectr

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

open class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startDi()
    }

    protected open fun startDi() {
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(appModules)
        }
    }
}