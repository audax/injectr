package net.daxbau.injectr.data

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagedList
import androidx.paging.toLiveData
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import net.daxbau.injectr.block
import net.daxbau.injectr.shouldEq
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.inject
import java.util.*


@RunWith(AndroidJUnit4::class)
class RoomTest : KoinTest {

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val context: Context by inject()

    private val db = Room.inMemoryDatabaseBuilder(context,
        AppDatabase::class.java).allowMainThreadQueries().build()

    private val injectionInfoDao = db.injectionInfoDao()

    @Test
    fun injectionInfoBasicOperations() {
        val injectionInfo = InjectionInfo(0, Date(), 1, 1, 1, "comment")
        injectionInfoDao.insertAll(injectionInfo)
        val data = injectionInfoDao.getAll().block()
        val element = injectionInfo.copy(id = 1)
        data shouldEq listOf(element)
        injectionInfoDao.delete(element)
        injectionInfoDao.getAll().block() shouldEq listOf()
    }

    @Test
    fun injectionInfoCanBePaginated() {
        val injectionInfo = InjectionInfo(0, Date(), 1, 1, 1, "comment")
        injectionInfoDao.insertAll(injectionInfo)
        val data: PagedList<InjectionInfo>? = injectionInfoDao.getPaginated().toLiveData(pageSize = 5).block()
        val element = injectionInfo.copy(id = 1)
        data shouldEq listOf(element)
    }


}