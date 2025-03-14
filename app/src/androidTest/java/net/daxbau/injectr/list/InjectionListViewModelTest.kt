package net.daxbau.injectr.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import net.daxbau.injectr.R
import net.daxbau.injectr.block
import net.daxbau.injectr.data.AppDatabase
import net.daxbau.injectr.data.InjectionInfo
import net.daxbau.injectr.runTest
import net.daxbau.injectr.shouldEq
import org.junit.Rule
import org.junit.Test
import java.util.*

class InjectionListViewModelTest {

    private val db = Room.inMemoryDatabaseBuilder(
        InstrumentationRegistry.getInstrumentation().targetContext,
        AppDatabase::class.java
    ).allowMainThreadQueries().build()
    private val injectionInfoDao = db.injectionInfoDao()

    private val mockNav = mock<NavController>()
    private val vm = InjectionListViewModelImpl(injectionInfoDao).apply {
        setNavController(mockNav)
    }
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun deletesInjectionInfo() = runTest {
        val item = InjectionInfo(1, Date(), 6, 1, 2, "A")
        injectionInfoDao.insertAll(item)
        vm.deleteInjection(item)
        assert(injectionInfoDao.getAllEager().isEmpty())
    }

    @Test
    fun doesNotNavigateAfterDestroy() {
        vm.onDestroy()
        vm.addInjection()
        verifyNoMoreInteractions(mockNav)
    }

    @Test
    fun navigatesToInjectView() {
        vm.addInjection()
        verify(mockNav).navigate(R.id.inject)
    }

    @Test
    fun exposesPaginatedLiveData() = runTest {
        val injectionInfo = InjectionInfo(1, Date(), 1, 2, 3, "D")
        injectionInfoDao.insertAll(injectionInfo)
        vm.injectionList.block()?.get(0) shouldEq injectionInfo
    }
}