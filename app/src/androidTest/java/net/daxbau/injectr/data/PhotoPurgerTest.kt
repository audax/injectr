package net.daxbau.injectr.data

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import net.daxbau.injectr.runTest
import net.daxbau.injectr.shouldEq
import org.junit.Test
import java.io.File
import java.io.FileNotFoundException
import java.util.*

class PhotoPurgerTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext
    private val db = Room.inMemoryDatabaseBuilder(
        context, AppDatabase::class.java
    ).allowMainThreadQueries().build()
    private val injectionInfoDao = db.injectionInfoDao()

    private val purger = PhotoPurgerImpl(injectionInfoDao, context)

    @Test
    fun purgeOldPhotos() = runTest {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val randomId = UUID.randomUUID().toString()
        val name = "injection_${randomId}_.jpg"
        val file = File(context.filesDir, name)
        file.writeText("I am a test")
        val oldTime = Calendar.Builder().setDate(2000, 10, 10).build().time
        val oldInjectionInfo = InjectionInfo(
            0, oldTime, 1, 1, 1,
            "comment", photoFileName = name
        )
        injectionInfoDao.insertAll(oldInjectionInfo)

        purger.purgePhotos()

        try {
            file.readText()
            assert(false)
        } catch (e: FileNotFoundException) {
        }
        injectionInfoDao.getAllEager().first() shouldEq oldInjectionInfo.copy(
            id = 1,
            photoFileName = null
        )
    }
}