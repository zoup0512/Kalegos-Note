package net.gsantner.opoc.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import net.gsantner.markor.App
import net.gsantner.opoc.bean.MdFile
import net.gsantner.opoc.bean.Dir

@Database(entities = [MdFile::class, Dir::class], version = 1)
abstract class MyDatabase : RoomDatabase() {
    abstract val fileDAO: MdFileDAO
    abstract val dirDAO: DirDAO

    companion object {

        @Volatile
        private var instance: MyDatabase? = null

        fun getInstance() = instance ?: synchronized(MyDatabase::class.java) {
            instance ?: buildDatabase().also { instance = it }
        }

        private fun buildDatabase(): MyDatabase =
            Room.databaseBuilder(App.get(), MyDatabase::class.java, "file_sys.db")
                .allowMainThreadQueries()
                .build()

    }
}