package net.gsantner.opoc.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.gsantner.opoc.bean.MdFile

@Dao
interface MdFileDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(file: MdFile)

    @Query("SELECT * FROM md_file WHERE fileId=:fileId")
    fun queryFileById(fileId: Int): MdFile

    @Query("SELECT * FROM md_file WHERE belongedDirId=:belongedDirId")
    fun queryFileListByDir(belongedDirId: Int): List<MdFile>
}