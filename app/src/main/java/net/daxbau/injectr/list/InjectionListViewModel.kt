package net.daxbau.injectr.list

import androidx.lifecycle.LiveData
import net.daxbau.injectr.R
import net.daxbau.injectr.common.NavigatingViewModel
import net.daxbau.injectr.data.InjectionInfo
import net.daxbau.injectr.data.InjectionInfoDao

abstract class InjectionListViewModel : NavigatingViewModel() {
    abstract val injectionList: LiveData<List<InjectionInfo>>
    abstract fun addInjection()
}

class InjectionListViewModelImpl (injectionInfoDao: InjectionInfoDao): InjectionListViewModel() {

    override val injectionList: LiveData<List<InjectionInfo>> = injectionInfoDao.getAll()

    override fun addInjection() {
        info("Navigating to inject view")
        nav?.navigate(R.id.inject)
    }

}
