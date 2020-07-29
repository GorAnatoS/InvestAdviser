package com.invest.advisor.data.db.secList

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Created by qsufff on 7/28/2020.
 */

data class SecEntry(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "SECID")
    @SerializedName("SECID")
    val secid: String
)
