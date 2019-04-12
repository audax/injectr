package net.daxbau.injectr.inject

import net.daxbau.injectr.appModules
import org.junit.Before

import org.junit.Assert.*
import org.koin.core.context.startKoin
import org.koin.test.AutoCloseKoinTest

class InjectViewModelTest : AutoCloseKoinTest() {

    @Before
    fun setUp() {
        startKoin {
            modules(appModules)
        }
    }
}