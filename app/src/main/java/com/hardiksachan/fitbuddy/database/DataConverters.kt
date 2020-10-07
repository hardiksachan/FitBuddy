package com.hardiksachan.fitbuddy.database

import androidx.room.TypeConverter
import java.util.*

private const val SEPARATOR = "\t@*1248###78"

class ListConverter {
    companion object {

        @TypeConverter
        @JvmStatic
        fun intsToString(ints: MutableList<Int>): String =
            ints.joinToString(separator = SEPARATOR) {
                it.toString()
            }

        @TypeConverter
        @JvmStatic
        fun stringToInt(str: String?): MutableList<Int> =
            str?.split(SEPARATOR)?.map {
                it.toIntOrNull() ?: -1
            }?.toMutableList() ?: arrayListOf(-1)
    }
}

class DateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}