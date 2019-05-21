package net.daxbau.injectr.inject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.fotoapparat.result.PhotoResult
import net.daxbau.injectr.R
import net.daxbau.injectr.common.NavigatingViewModel
import net.daxbau.injectr.data.InjectionInfo
import net.daxbau.injectr.data.InjectionInfoDao
import java.util.*

abstract class InjectViewModel : NavigatingViewModel() {
    abstract var depth: Int
    abstract var limb: Int
    abstract var position: Int
    abstract var date: Date?
    abstract var comment: String
    abstract var photo: PhotoResult?
    abstract val confirmationRequired: LiveData<Boolean>
    abstract suspend fun save()
    abstract suspend fun confirmSave()
    abstract fun switchCamera()
    abstract fun toggleTorch()
    abstract fun loadInjectionInfo(injectionInfo: InjectionInfo)
}

class InjectViewModelImpl(private val injectionInfoDao: InjectionInfoDao, private val photoManager: PhotoManager)
    : InjectViewModel() {

    override var depth: Int = 0
    override var limb: Int = 0
    override var position: Int = 0
    override var date: Date? = null
    override var comment: String = ""
    override var photo: PhotoResult? = null

    private val _confirmationRequired = MutableLiveData<Boolean>().apply { value = false }
    override val confirmationRequired: LiveData<Boolean> = _confirmationRequired

    private var injectionInfo: InjectionInfo? = null

    override suspend fun save() {
        internalSave()
    }

    override fun loadInjectionInfo(injectionInfo: InjectionInfo) {
        this.injectionInfo = injectionInfo
        depth = injectionInfo.depth
        limb = injectionInfo.limb
        position = injectionInfo.position
        date = injectionInfo.date
        comment = injectionInfo.comment
    }

    private suspend fun internalSave(force: Boolean = false) {
        val fileName = try {
            photoManager.save()
        } catch (e: NoPhotoAvailableError) {
            if (!force) {
                _confirmationRequired.postValue(true)
                return
            } else {
                null
            }
        }
        val injectionDate = date ?: run {
            val now = Date()
            date = now
            now
        }
        injectionInfoDao.insertAll(InjectionInfo(0, injectionDate, depth, limb, position, comment, fileName))
        nav?.navigate(R.id.injectionList)
    }

    override suspend fun confirmSave() {
        _confirmationRequired.postValue(false)
        internalSave(true)
    }

    override fun switchCamera() {
        photoManager.switchCamera()
    }

    override fun toggleTorch() {
        photoManager.toggleTorch()
    }
}