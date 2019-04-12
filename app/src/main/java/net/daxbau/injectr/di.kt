package net.daxbau.injectr

import androidx.room.Room
import net.daxbau.injectr.data.AppDatabase
import net.daxbau.injectr.inject.InjectViewModel
import net.daxbau.injectr.inject.InjectViewModelImpl
import net.daxbau.injectr.list.InjectionListViewModel
import net.daxbau.injectr.list.InjectionListViewModelImpl
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { get<AppDatabase>().injectionInfoDao() }
    viewModel<InjectionListViewModel> { InjectionListViewModelImpl() }
    viewModel<InjectViewModel> { InjectViewModelImpl() }
}

val productionModule = module {
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "database"
            ).build()
    }
}

val appModules = listOf(
    appModule,
    productionModule
)

