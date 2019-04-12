package net.daxbau.injectr.list

import com.nhaarman.mockitokotlin2.spy
import net.daxbau.injectr.BaseFragmentTest
import net.daxbau.injectr.R
import org.junit.Before

import org.junit.Assert.*
import org.koin.test.mock.declare

class InjectionListTest : BaseFragmentTest() {
    override val fragmentId = R.id.injectionList

    private val vm = spy<StubInjectionListViewModel>()

    override fun installMocks() {
        declare {
            single<InjectionListViewModel>(override = true) { vm }
        }
    }

    @Before
    fun setUp() {
    }

    private open class StubInjectionListViewModel : InjectionListViewModel()
}