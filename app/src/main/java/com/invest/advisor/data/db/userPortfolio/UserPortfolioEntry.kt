package com.invest.advisor.data.db.userPortfolio

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = TABLE_NAME )
data class UserPortfolioEntry(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Id_COLUMN)
    val id: Int,

    @ColumnInfo(name = secId_COLUMN)
    val secId: String,
    /*@ColumnInfo(name = secName_COLUMN)
    var secName: String?,*/
    @ColumnInfo(name = secPrice_COLUMN)
    val secPrice: String,

    @ColumnInfo(name = secQuantity_COLUMN)
    val secQuantity: Int,

    @ColumnInfo(name = secBuyDate_COLUMN)
    val secPurchaseDate: Long
)

const val DATABASE_NAME = "User_s_portfolio_database.db"
const val TABLE_NAME = "User_s_portfolio"
const val Id_COLUMN = "id"
const val secId_COLUMN = "secId"
const val secBuyDate_COLUMN = "date of purchase"
const val secPrice_COLUMN = "secPrice"
const val secQuantity_COLUMN = "Quantity"

const val DATABASE_VERSION = 1


