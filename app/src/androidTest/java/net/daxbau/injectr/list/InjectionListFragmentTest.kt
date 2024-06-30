package net.daxbau.injectr.list

import androidx.paging.toLiveData
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.adevinta.android.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.present
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.delay
import net.daxbau.injectr.BaseFragmentTest
import net.daxbau.injectr.R
import net.daxbau.injectr.data.AppDatabase
import net.daxbau.injectr.data.InjectionInfo
import net.daxbau.injectr.data.InjectionInfoDao
import net.daxbau.injectr.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.mock.declare
import org.koin.test.mock.declareMock
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
        declare<InjectionListViewModel> { vm }
    }

    @Test
    fun testLaunch() = runTest {
        launchActivity()
        assertThat(activityRule.activity.nav, present())
        verify(vm, atLeastOnce()).setNavController(activityRule.activity.nav)
        activityRule.finishActivity()
        delay(300)
        verify(vm, atLeastOnce()).onDestroy()
    }

    @Test
    fun fabIsConnected() {
        launchActivity()
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
        launchActivity()
        assertRecyclerViewItemCount(R.id.injectionListRecyclerView, 3)
    }

    private open class StubInjectionListFragmentViewModel(injectionInfoDao: InjectionInfoDao) :
        InjectionListViewModel() {
        override val injectionList = injectionInfoDao.getPaginated().toLiveData(pageSize = 5)
        override fun addInjection() { }
    }
}