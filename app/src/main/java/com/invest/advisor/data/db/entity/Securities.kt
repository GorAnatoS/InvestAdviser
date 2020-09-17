package com.invest.advisor.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

//http://iss.moex.com/iss/engines/stock/markets/shares/boards/TQBR/securities/columns

@Entity(tableName = "securities_data")
data class Securities(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    @TypeConverters(ColumnsDataConverter::class)
    val columns: List<String>,
    @TypeConverters(ColumnsDataConverter::class)
    val `data`: List<List<String>>
)

enum class EnumSecurities(val rowName: String, rowType: Any){
    SECID("SECID", String),     //тикер
    BOARDID("BOARDID",String),
    SHORTNAME("SHORTNAME", String),
    PREVPRICE("PREVPRICE", Double),
    LOTSIZE("LOTSIZE",  Int), //Int32
    FACEVALUE("FACEVALUE", Double),
    STATUS("STATUS", String),
    BOARDNAME("BOARDNAME", String),
    DECIMALS("DECIMALS", Int), //Int32
    SECNAME("SECNAME", String),
    REMARKS("REMARKS", String),
    MARKETCODE("MARKETCODE", String),
    INSTRID("INSTRID", String),
    SECTORID("SECTORID", String),
    MINSTEP("MINSTEP", Double),
    PREVWAPRICE("PREVWAPRICE", Double),
    FACEUNIT("FACEUNIT", String),
    PREVDATE("PREVDATE" , String), //date
    ISSUESIZE("ISSUESIZE", Int), //Int64
    ISIN("ISIN", String),
    LATNAME("LATNAME", String),
    REGNUMBER("REGNUMBER", String),
    PREVLEGALCLOSEPRICE("PREVLEGALCLOSEPRICE", Double),
    PREVADMITTEDQUOTE("PREVADMITTEDQUOTE", Double),
    CURRENCYID("CURRENCYID", String),
    SECTYPE("SECTYPE", String),
    LISTLEVEL("LISTLEVEL", Int), //Int32
    SETTLEDATE("SETTLEDATE" , String) //date
}