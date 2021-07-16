package com.invest.advisor.data.db.database.userPortfolio

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
Entries of user's portfolio that contains:
secId - short name of share
secPrice - what price was when share was bought
secQuantity - how many share user bought
secPurchaseDate - when user bought share
*/

@Entity(tableName = USER_PORTFOLIO_TABLE_NAME)
data class UserPortfolioEntry(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Id_COLUMN)
    val id: Int,

    @ColumnInfo(name = secId_COLUMN)
    val secId: String,

    @ColumnInfo(name = secPrice_COLUMN)
    val secPrice: String,

    @ColumnInfo(name = secQuantity_COLUMN)
    val secQuantity: Int,

    @ColumnInfo(name = secBuyDate_COLUMN)
    val secPurchaseDate: Long
)

const val USER_PORTFOLIO_DATABASE_NAME = "user_portfolio_database.db"
const val USER_PORTFOLIO_TABLE_NAME = "user_portfolio_table"
const val Id_COLUMN = "id"
const val secId_COLUMN = "secId"
const val secBuyDate_COLUMN = "date of purchase"
const val secPrice_COLUMN = "secPrice"
const val secQuantity_COLUMN = "Quantity"

const val USER_PORTFOLIO_DATABASE_VERSION = 1


