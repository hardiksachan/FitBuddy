package com.hardiksachan.fitbuddy.database

import androidx.room.TypeConverter

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