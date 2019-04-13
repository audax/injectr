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
    private val date = Date()
    private val sampleInjection = InjectionInfo(0, date, 4, 1, 1, "comment")
    private val vm = InjectViewModelImpl(dao, photoManager).apply {
        setNavController(nav)
    }

    @Test
    fun `saves the model with new date`() = runTest {
        fillFields()
        verify(dao).insertAll(sampleInjection.copy(date = vm.date!!))
        verify(nav).navigate(R.id.injectionList)
    }

    private suspend fun fillFields() {
        vm.depth = 4
        vm.limb = 1
        vm.position = 1
        vm.comment = "comment"
        vm.save()
        delay(50)
    }

    @Test
    fun `saves the model with supplied date`() = runTest {
        vm.date = date
        fillFields()
        verify(dao).insertAll(sampleInjection)
        verify(nav).navigate(R.id.injectionList)
    }

    @Test
    fun `saves the photo`() = runTest {
        photoManager.bitmap = mockBitmap
        vm.date = date
        fillFields()
        verify(dao).insertAll(sampleInjection.copy(photoFileName = photoManager.fileName))
        verify(nav).navigate(R.id.injectionList)
    }
}