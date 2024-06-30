package net.daxbau.injectr

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.mock
import org.junit.Rule
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.test.KoinTest
import org.koin.test.check.checkModules

class DiModulesTest : KoinTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()
    private val context = mock<Context>()

    @Test
    fun testModules() {
        startKoin {
            androidContext(context)
            modules(appModules)
        }.checkModules()
    }
}