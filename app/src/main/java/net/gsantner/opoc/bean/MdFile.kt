package net.gsantner.opoc.bean

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "md_file")
@TypeConverters(value = [TagConverter::class])
data class MdFile(
    @PrimaryKey(autoGenerate = true)
    var fileId:Int?,
    var name:String?=null,
    var desc:String?=null,
    var belongedDirId:Int?=null,
    var author:String?=null,
    var isOriginal:Boolean=false,
    var reprintFrom:String?=null,
    var filePath:String?=null,
    var icon:String?=null,
    var tags:List<String>?,
    var date:Long=System.currentTimeMillis()
)
