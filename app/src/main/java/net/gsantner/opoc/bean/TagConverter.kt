package net.gsantner.opoc.bean

import androidx.room.TypeConverter
import java.lang.StringBuilder


class TagConverter {
    @TypeConverter
    fun listToString(value: String?): List<String>? {
        if (value != null) {
            return value.split(",")
        }
        return null
    }

    @TypeConverter
    fun stringToList(list: List<String>?): String? {

        return if (list.isNullOrEmpty()) {
            null
        } else {
            val str = StringBuilder(list[0])
            list.forEach {
                str.append(",").append(it)
            }
            str.toString();
        }
    }
}