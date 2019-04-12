package net.daxbau.injectr.inject

import com.nhaarman.mockitokotlin2.spy
import net.daxbau.injectr.BaseFragmentTest
import net.daxbau.injectr.R
import org.junit.Before

import org.junit.Assert.*
import org.koin.test.mock.declare

class InjectTest : BaseFragmentTest() {
    override val fragmentId = R.id.inject

    private val vm = spy<StubInjectionListViewModel>()

    override fun installMocks() {
        declare {
            single<InjectViewModel>(override = true) { vm }
        }
    }

    @Before
    fun setUp() {
    }

    private open class StubInjectionListViewModel : InjectViewModel()
}