package net.daxbau.injectr.list

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.present
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import net.daxbau.injectr.BaseFragmentTest
import net.daxbau.injectr.R
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.mock.declare

@RunWith(AndroidJUnit4::class)
class InjectionListTest : BaseFragmentTest() {
    override val fragmentId = R.id.injectionList

    private val vm = spy<StubInjectionListViewModel>()

    override fun installMocks() {
        declare {
            single<InjectionListViewModel>(override = true) { vm }
        }
    }

    @Test
    fun testLaunch() {
        launch()
        assertThat(activityRule.activity.nav, present())
        verify(vm, atLeastOnce()).setNavController(activityRule.activity.nav)
        activityRule.finishActivity()
        verify(vm, atLeastOnce()).onDestroy()
    }

    @Test
    fun fabIsConnected() {
        launch()
        clickOn(R.id.injectFab)
        verify(vm).addInjection()
    }

    private open class StubInjectionListViewModel : InjectionListViewModel() {
        override fun addInjection() { }
    }
}