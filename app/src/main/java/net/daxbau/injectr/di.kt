package net.daxbau.injectr

import androidx.room.Room
import net.daxbau.injectr.data.AppDatabase
import net.daxbau.injectr.data.PhotoPurger
import net.daxbau.injectr.data.PhotoPurgerImpl
import net.daxbau.injectr.inject.FotoapparatPhotoManager
import net.daxbau.injectr.inject.InjectViewModel
import net.daxbau.injectr.inject.InjectViewModelImpl
import net.daxbau.injectr.inject.PhotoManager
import net.daxbau.injectr.list.InjectionListViewModel
import net.daxbau.injectr.list.InjectionListViewModelImpl
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { get<AppDatabase>().injectionInfoDao() }
    viewModel<MainActivityViewModel> { MainActivityViewModelImpl(get()) }
    viewModel<InjectionListViewModel> { InjectionListViewModelImpl(get()) }
    viewModel<InjectViewModel> { InjectViewModelImpl(get(), get()) }
    single<PhotoManager> { FotoapparatPhotoManager(get()) }
    single<PhotoPurger> { PhotoPurgerImpl(get(), get()) }
}

val productionModule = module {
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            "database"
            ).fallbackToDestructiveMigration().build()
    }
}

val appModules = listOf(
    appModule,
    productionModule
)

