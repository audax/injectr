package net.daxbau.injectr.list

import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import net.daxbau.injectr.R
import net.daxbau.injectr.data.InjectionInfo
import net.daxbau.injectr.data.InjectionInfoDao
import net.daxbau.injectr.shouldEq
import org.junit.Test

class InjectionListViewModelTest {

    private val injectionList = mock<MutableLiveData<List<InjectionInfo>>>()
    private val injectionInfoDao = mock<InjectionInfoDao> {
        on { getAll() } doReturn injectionList
    }
    private val mockNav = mock<NavController>()
    private val vm = InjectionListViewModelImpl(injectionInfoDao).apply {
        setNavController(mockNav)
    }

    @Test
    fun `does not navigate after destroy`() {
        vm.onDestroy()
        vm.addInjection()
        verifyNoMoreInteractions(mockNav)
    }

    @Test
    fun `navigates to inject view`() {
        vm.addInjection()
        verify(mockNav).navigate(R.id.inject)
    }

    @Test
    fun `exposes live data`() {
        vm.injectionList shouldEq injectionList
    }

}