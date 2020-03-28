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

    private val injectionInfo = InjectionInfo(0, Date(), 1, 1, 1, "comment")
    private val oldTime = Calendar.Builder().setDate(2000, 10, 10).build().time
    private val oldInjectionInfo =
        InjectionInfo(0, oldTime, 1, 1, 1, "comment", photoFileName = "foo.png")
    @Test
    fun injectionInfoBasicOperations() {
        injectionInfoDao.insertAll(injectionInfo)
        val data = injectionInfoDao.getAll().block()
        val element = injectionInfo.copy(id = 1)
        data shouldEq listOf(element)
        injectionInfoDao.delete(element)
        injectionInfoDao.getAll().block() shouldEq listOf()
    }

    @Test
    fun injectionInfoCanBePaginated() {
        injectionInfoDao.insertAll(injectionInfo)
        val data: PagedList<InjectionInfo>? = injectionInfoDao.getPaginated().toLiveData(pageSize = 5).block()
        val element = injectionInfo.copy(id = 1)
        data shouldEq listOf(element)
    }

    @Test
    fun findOldInjections() {
        injectionInfoDao.insertAll(injectionInfo, oldInjectionInfo)
        injectionInfoDao.findOlderThan(
            Calendar.Builder().setDate(2001, 10, 10).build().time
        ) shouldEq listOf(oldInjectionInfo.copy(id = 2))
    }

    @Test
    fun updateInjections() {
        injectionInfoDao.insertAll(oldInjectionInfo)
        val inserted = injectionInfoDao.getAll().block()!!.first()
        val update = inserted.copy(photoFileName = null)
        injectionInfoDao.updateAll(update)
        injectionInfoDao.getAll().block()!!.first() shouldEq update
    }

}