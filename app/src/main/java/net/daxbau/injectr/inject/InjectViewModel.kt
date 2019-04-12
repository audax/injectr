package net.daxbau.injectr.inject

import android.content.Context
import io.fotoapparat.result.PhotoResult
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.daxbau.injectr.R
import net.daxbau.injectr.common.NavigatingViewModel
import net.daxbau.injectr.data.InjectionInfo
import net.daxbau.injectr.data.InjectionInfoDao
import java.io.File
import java.util.*

abstract class InjectViewModel : NavigatingViewModel() {
    abstract var depth: Int
    abstract var date: Date?
    abstract var photo: PhotoResult?
    abstract fun save()
}

class InjectViewModelImpl(private val injectionInfoDao: InjectionInfoDao, private val context: Context): InjectViewModel() {
    override var depth: Int = 0
    override var date: Date? = null
    override var photo: PhotoResult? = null

    override fun save() {
        val injectionDate = date ?: run {
            val now = Date()
            date = now
            now
        }
        val currentPhoto = photo
        if (currentPhoto !== null) {
            val randomId = UUID.randomUUID().toString()
            val name = "injection_${randomId}_.jpg"
            val file = File(context.filesDir, name)
            currentPhoto.saveToFile(file).whenAvailable {
                GlobalScope.launch {
                    injectionInfoDao.insertAll(InjectionInfo(0, injectionDate, depth))
                    nav?.navigate(R.id.injectionList)
                }
            }
        }
    }
}