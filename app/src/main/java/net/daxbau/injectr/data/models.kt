package net.daxbau.injectr.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*


@Parcelize
@Entity(tableName = "injection_info", indices = [Index(value = ["date"])])
data class InjectionInfo(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val date: Date,
    val depth: Int,
    val limb: Int,
    val position: Int,
    val comment: String,
    val photoFileName: String? = null
) : Parcelable {
    fun position() =  limb.toString() + ('A' + position).toString()
}

