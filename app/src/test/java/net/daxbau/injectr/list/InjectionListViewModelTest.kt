package net.daxbau.injectr.list

import net.daxbau.injectr.appModules
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.koin.core.context.startKoin
import org.koin.test.AutoCloseKoinTest

class InjectionListViewModelTest : AutoCloseKoinTest() {

    @Before
    fun setUp() {
        startKoin {
            modules(appModules)
        }
    }

}