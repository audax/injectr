package net.daxbau.injectr

import androidx.room.Room
import net.daxbau.injectr.data.AppDatabase
import org.koin.dsl.module

val testModule = module {
    single {
        Room.inMemoryDatabaseBuilder(
            get(),
            AppDatabase::class.java
        ).build()
    }
}

val testModules = listOf(
    appModule,
    testModule
)