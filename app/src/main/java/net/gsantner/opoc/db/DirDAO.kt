package net.gsantner.opoc.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.gsantner.opoc.bean.Dir

@Dao
interface DirDAO {
    @Insert(onConflict=OnConflictStrategy.REPLACE)
    fun insertDir(vararg dir: Dir)

    @Query("SELECT * FROM dir WHERE isRootDir=1")
    fun queryRootDirList(): List<Dir>

    @Query("SELECT * FROM dir WHERE id=:id")
    fun queryDirById(id: Int): Dir
}