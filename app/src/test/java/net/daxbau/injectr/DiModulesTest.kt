package net.daxbau.injectr

import android.content.Context
import com.nhaarman.mockitokotlin2.mock
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.test.AutoCloseKoinTest
import org.koin.test.check.checkModules

class DiModulesTest : AutoCloseKoinTest() {

    private val context = mock<Context>()

    @Test
    fun testModules() {
        startKoin {
            androidContext(context)
            modules(appModules)
        }.checkModules()
    }
}