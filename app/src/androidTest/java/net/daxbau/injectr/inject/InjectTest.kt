package net.daxbau.injectr.inject

import androidx.test.runner.AndroidJUnit4
import com.nhaarman.mockitokotlin2.spy
import net.daxbau.injectr.BaseFragmentTest
import net.daxbau.injectr.R
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.mock.declare

@RunWith(AndroidJUnit4::class)
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

    @Test
    fun testLaunch() {
        launch()
    }

    private open class StubInjectionListViewModel : InjectViewModel()
}