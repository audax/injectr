package net.daxbau.injectr.data

import androidx.room.*
import java.util.*


@Entity(tableName = "injection_info")
data class InjectionInfo(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val date: Date,
    val depth: Int
)

