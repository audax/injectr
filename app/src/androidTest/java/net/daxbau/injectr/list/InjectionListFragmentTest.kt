package net.daxbau.injectr.list

import androidx.paging.toLiveData
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.present
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import com.schibsted.spain.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import net.daxbau.injectr.BaseFragmentTest
import net.daxbau.injectr.R
import net.daxbau.injectr.data.AppDatabase
import net.daxbau.injectr.data.InjectionInfo
import net.daxbau.injectr.data.InjectionInfoDao
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.mock.declare
import java.util.*

@RunWith(AndroidJUnit4::class)
class InjectionListFragmentTest : BaseFragmentTest() {
    override val fragmentId = R.id.injectionList

    private val db = Room.inMemoryDatabaseBuilder(
        InstrumentationRegistry.getInstrumentation().targetContext,
        AppDatabase::class.java
    ).allowMainThreadQueries().build()
    private val injectionInfoDao = db.injectionInfoDao()

    private val vm = spy(StubInjectionListFragmentViewModel(injectionInfoDao))

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
        injectionInfoDao.insertAll(
            InjectionInfo(1, Date(), 6, 1, 2, "A"),
            InjectionInfo(2, Date(), 8, 1, 1, "B"),
            InjectionInfo(3, Date(), 2, 2, 4, "C")
        )
        launch()
        assertRecyclerViewItemCount(R.id.injectionListRecyclerView, 3)
    }

    private open class StubInjectionListFragmentViewModel(injectionInfoDao: InjectionInfoDao) :
        InjectionListViewModel() {
        override val injectionList = injectionInfoDao.getPaginated().toLiveData(pageSize = 5)
        override fun addInjection() { }
    }
}