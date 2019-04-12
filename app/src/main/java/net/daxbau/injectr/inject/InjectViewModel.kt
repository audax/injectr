package net.daxbau.injectr.inject

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.daxbau.injectr.R
import net.daxbau.injectr.common.NavigatingViewModel
import net.daxbau.injectr.data.InjectionInfo
import net.daxbau.injectr.data.InjectionInfoDao
import java.util.*

abstract class InjectViewModel : NavigatingViewModel() {
    abstract var depth: Int
    abstract var date: Date?
    abstract fun save()
}

class InjectViewModelImpl(private val injectionInfoDao: InjectionInfoDao): InjectViewModel() {
    override var depth: Int = 0
    override var date: Date? = null

    override fun save() {
        val injectionDate = date ?: run {
            val now = Date()
            date = now
            now
        }
        GlobalScope.launch {
            injectionInfoDao.insertAll(InjectionInfo(0, injectionDate, depth))
            nav?.navigate(R.id.injectionList)
        }
    }
}