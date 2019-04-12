package net.daxbau.injectr.list

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import net.daxbau.injectr.R
import net.daxbau.injectr.common.JustLog
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
