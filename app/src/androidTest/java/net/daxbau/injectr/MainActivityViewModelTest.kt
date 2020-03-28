package net.daxbau.injectr

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import net.daxbau.injectr.data.PhotoPurger
import org.junit.Test

class MainActivityViewModelTest {

    private val purger = mock<PhotoPurger>()
    private val vm = MainActivityViewModelImpl(purger)

    @Test
    fun startsPurge() = runTest {
        vm.purgeOldPhotos()
        verify(purger).purgePhotos()
    }
}