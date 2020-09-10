package com.invest.advisor.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

data class MoexEntry(
    val secId: String?,
    var secName: String?,
    val secPrice: String?,
    val secChange: String?
)
