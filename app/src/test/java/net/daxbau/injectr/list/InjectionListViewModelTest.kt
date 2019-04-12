package net.daxbau.injectr.list

import androidx.navigation.NavController
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import net.daxbau.injectr.R
import org.junit.Test

class InjectionListViewModelTest {

    private val mockNav = mock<NavController>()
    private val vm = InjectionListViewModelImpl().apply {
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

}