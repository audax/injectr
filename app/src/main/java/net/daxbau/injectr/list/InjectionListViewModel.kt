package net.daxbau.injectr.list

import net.daxbau.injectr.R
import net.daxbau.injectr.common.NavigatingViewModel

abstract class InjectionListViewModel : NavigatingViewModel() {
    abstract fun addInjection()
}

class InjectionListViewModelImpl : InjectionListViewModel() {

    override fun addInjection() {
        info("Navigating to inject view")
        nav?.navigate(R.id.inject)
    }

}
