package com.invest.advisor.data.db.entity

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.util.*
import java.util.stream.Collectors


/**
 * Created by qsufff on 7/29/2020.
 */

class ColumnsDataConverter {
    //columns
    @RequiresApi(Build.VERSION_CODES.N)
    @TypeConverter
    fun fromColumns(columns: List<String>): String {
        return columns.stream().collect(Collectors.joining(","))
    }

    @TypeConverter
    fun toColumns(columns: String): List<String> {
        return listOf(columns)
    }

    //data
    @RequiresApi(Build.VERSION_CODES.N)
    @TypeConverter
    fun fromData(data: List<List<String>>): String {
        return data[0].stream().collect(Collectors.joining(","))
    }

    @TypeConverter
    fun toData(data: String): List<List<String>> {
        return listOf(listOf(data))
    }
}