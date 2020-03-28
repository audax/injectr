package net.daxbau.injectr

import androidx.lifecycle.ViewModel
import net.daxbau.injectr.data.PhotoPurger

abstract class MainActivityViewModel : ViewModel() {
    abstract suspend fun purgeOldPhotos()
}

class MainActivityViewModelImpl(private val photoPurger: PhotoPurger) : MainActivityViewModel() {
    override suspend fun purgeOldPhotos() {
        photoPurger.purgePhotos()
    }
}