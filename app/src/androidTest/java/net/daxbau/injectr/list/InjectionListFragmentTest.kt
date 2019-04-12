package net.daxbau.injectr.list

import androidx.lifecycle.MutableLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.present
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import com.schibsted.spain.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import net.daxbau.injectr.BaseFragmentTest
import net.daxbau.injectr.R
import net.daxbau.injectr.data.InjectionInfo
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.mock.declare
import java.util.*

@RunWith(AndroidJUnit4::class)
class InjectionListFragmentTest : BaseFragmentTest() {
    override val fragmentId = R.id.injectionList

    private val vm = spy<StubInjectionListFragmentViewModel>()

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

    @Test
    fun showsData() {
        val list = listOf(
            InjectionInfo(1, Date(), 6),
            InjectionInfo(2, Date(), 8),
            InjectionInfo(3, Date(), 2)
        )
        vm.mutableInjectionInfo.postValue(list)
        launch()
        assertRecyclerViewItemCount(R.id.injectionListRecyclerView, 3)
    }

    private open class StubInjectionListFragmentViewModel : InjectionListViewModel() {
        val mutableInjectionInfo = MutableLiveData<List<InjectionInfo>>()
        override val injectionList = mutableInjectionInfo
        override fun addInjection() { }
    }
}