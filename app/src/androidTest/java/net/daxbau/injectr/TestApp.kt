package net.daxbau.injectr

import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TestApp : App() {

    override fun startDi() {
        startKoin {
            androidLogger()
            androidContext(this@TestApp)
            modules(appModule)
        }
    }
}