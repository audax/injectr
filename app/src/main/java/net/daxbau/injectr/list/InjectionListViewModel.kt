package net.daxbau.injectr.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Config
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.daxbau.injectr.R
import net.daxbau.injectr.common.NavigatingViewModel
import net.daxbau.injectr.data.InjectionInfo
import net.daxbau.injectr.data.InjectionInfoDao

abstract class InjectionListViewModel : NavigatingViewModel() {
    abstract val injectionList: LiveData<PagedList<InjectionInfo>>
    abstract fun addInjection()
    abstract fun deleteInjection(injectionInfo: InjectionInfo)
}

class InjectionListViewModelImpl(
    private val injectionInfoDao: InjectionInfoDao
) : InjectionListViewModel() {

    private val paginationConfig = Config(
        pageSize = 3,
        enablePlaceholders = false,
        initialLoadSizeHint = 3
    )
    override val injectionList: LiveData<PagedList<InjectionInfo>> =
        LivePagedListBuilder(injectionInfoDao.getPaginated(), paginationConfig)
            .build()

    override fun addInjection() {
        info("Navigating to inject view")
        nav?.navigate(R.id.inject)
    }

    override fun deleteInjection(injectionInfo: InjectionInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            injectionInfoDao.delete(injectionInfo)
        }
    }
}
