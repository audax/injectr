package net.daxbau.injectr.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import java.util.*

@Dao
interface InjectionInfoDao {
    @Query("SELECT * FROM injection_info ORDER BY date DESC")
    fun getAll(): LiveData<List<InjectionInfo>>

    @Query("SELECT * FROM injection_info ORDER BY date DESC")
    fun getPaginated(): DataSource.Factory<Int, InjectionInfo>

    @Insert
    fun insertAll(vararg injections: InjectionInfo)

    @Delete
    fun delete(injection: InjectionInfo)
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}


@Database(
    version = 4,
    entities = [InjectionInfo::class],
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun injectionInfoDao() : InjectionInfoDao
}
