package net.daxbau.injectr.inject

import android.graphics.Bitmap
import androidx.navigation.NavController
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.delay
import net.daxbau.injectr.R
import net.daxbau.injectr.common.StubPhotoManager
import net.daxbau.injectr.data.InjectionInfo
import net.daxbau.injectr.data.InjectionInfoDao
import net.daxbau.injectr.runTest
import org.junit.Test
import java.util.*

class InjectViewModelTest  {
    private val dao = mock<InjectionInfoDao>()
    private val nav = mock<NavController>()
    private val mockBitmap = mock<Bitmap>()
    private val photoManager = StubPhotoManager()
    private val vm = InjectViewModelImpl(dao, photoManager).apply {
        setNavController(nav)
    }

    @Test
    fun `saves the model with new date`() = runTest {
        vm.depth = 4
        vm.save()
        delay(50)
        verify(dao).insertAll(InjectionInfo(0, vm.date!!, 4))
        verify(nav).navigate(R.id.injectionList)
    }

    @Test
    fun `saves the model with supplied date`() = runTest {
        vm.depth = 4
        val date = Date()
        vm.date = date
        vm.save()
        delay(50)
        verify(dao).insertAll(InjectionInfo(0, date, 4))
        verify(nav).navigate(R.id.injectionList)
    }

    @Test
    fun `saves the photo`() = runTest {
        vm.depth = 4
        val date = Date()
        vm.date = date
        photoManager.bitmap = mockBitmap
        vm.save()
        delay(50)
        verify(dao).insertAll(InjectionInfo(0, date, 4, photoManager.fileName))
        verify(nav).navigate(R.id.injectionList)
    }
}