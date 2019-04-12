package net.daxbau.injectr

import net.daxbau.injectr.inject.InjectViewModel
import net.daxbau.injectr.inject.InjectViewModelImpl
import net.daxbau.injectr.list.InjectionListViewModel
import net.daxbau.injectr.list.InjectionListViewModelImpl
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel<InjectionListViewModel> { InjectionListViewModelImpl() }
    viewModel<InjectViewModel> { InjectViewModelImpl() }
}

val appModules = listOf(
    appModule
)
