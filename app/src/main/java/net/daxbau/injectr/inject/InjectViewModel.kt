package net.daxbau.injectr.inject

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
    abstract suspend fun save()
}

class InjectViewModelImpl(private val injectionInfoDao: InjectionInfoDao, private val photoManager: PhotoManager)
    : InjectViewModel() {
    override var depth: Int = 0
    override var limb: Int = 0
    override var position: Int = 0
    override var date: Date? = null
    override var comment: String = ""
    override var photo: PhotoResult? = null


    override suspend fun save() {
        val injectionDate = date ?: run {
            val now = Date()
            date = now
            now
        }
        val fileName = try {
            photoManager.save()
        } catch (e: NoPhotoAvailableError) {
            null
        }
        injectionInfoDao.insertAll(InjectionInfo(0, injectionDate, depth, limb, position, comment, fileName))
        nav?.navigate(R.id.injectionList)
    }
}