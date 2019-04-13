package net.daxbau.injectr.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*


@Entity(tableName = "injection_info")
data class InjectionInfo(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val date: Date,
    val depth: Int,
    val limb: Int,
    val position: Int,
    val comment: String,
    val photoFileName: String? = null
) {
    fun position() = ('A' + limb).toString() + position.toString()
}

