package net.gsantner.opoc.bean

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "dir")
@TypeConverters(value = [TagConverter::class])
data class Dir(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var dirId: Int?,
    var dirName: String?,
    var superDirId: Int?,
    var superDirName: String?,
    var tags: List<String>?,
    var isRootDir: Int? = 0,
    var desc: String? = null,
    var icon: String? = null,
    var date: Long = System.currentTimeMillis()
)

