package net.daxbau.injectr.data

import android.content.Context
import net.daxbau.injectr.common.JustLog
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

interface PhotoPurger {
    suspend fun purgePhotos()
}

class PhotoPurgerImpl(
    private val injectionInfoDao: InjectionInfoDao,
    private val context: Context
) : PhotoPurger, JustLog {
    override suspend fun purgePhotos() {
        val longtimeAgo = Date.from(
            LocalDateTime.now().minusMonths(3)
                .atZone(ZoneId.systemDefault()).toInstant()
        )
        for (injection in injectionInfoDao.findOlderThan(longtimeAgo)) {
            injection.photoFileName?.let { fileName ->
                assert(File(context.filesDir, fileName).delete())
                injectionInfoDao.updateAll(injection.copy(photoFileName = null))
                info("Delete photo $fileName")
            }
        }
    }

}