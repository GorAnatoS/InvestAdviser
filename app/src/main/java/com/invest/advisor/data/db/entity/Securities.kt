package com.invest.advisor.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "MOEX_data")
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

//package com.invest.advisor.data.db.entity

//import androidx.room.Entity
//import androidx.room.PrimaryKey

//const val CURRENT_MOEX_DATA_ID = 0
//
//data class MOEXdata(
//    val securities: Securities,
//    val marketdata: Marketdata
//)

/*
package com.invest.advisor.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters


@Entity(tableName = "MOEX_data")
data class Marketdata(
    @PrimaryKey(autoGenerate = false)
    val id: Int = 0,
    @TypeConverters(ColumnsDataConverter::class)
    val columns: List<String>,
    @TypeConverters(DataConverter::class)
    val `data`: List<List<Any>>
)

enum class EnumMarketData(val rowName: String, val rowType: Any){
    SECID("SECID", String),
    BOARDID("BOARDID", String),
    BID("BID", Double),
    BIDDEPTH("BIDDEPTH", String), //was undefined
    OFFER("OFFER", Double),
    OFFERDEPTH("OFFERDEPTH", String), //was undefined
    SPREAD("SPREAD", Double),
    BIDDEPTHT("BIDDEPTHT", Int), //int 32
    OFFERDEPTHT("OFFERDEPTHT", Int), //int 32
    OPEN("OPEN", Double),
    LOW("LOW", Double),
    HIGH("HIGH", Double),
    LAST("LAST", Double),
    LASTCHANGE("LASTCHANGE", Double),
    LASTCHANGEPRCNT("LASTCHANGEPRCNT", Double),
    QTY("QTY", Int), //int 32
    VALUE("VALUE", Double),
    VALUE_USD("VALUE_USD", Double),
    WAPRICE("WAPRICE", Double),
    LASTCNGTOLASTWAPRICE("LASTCNGTOLASTWAPRICE", Double),
    WAPTOPREVWAPRICEPRCNT("WAPTOPREVWAPRICEPRCNT", Double),
    WAPTOPREVWAPRICE("WAPTOPREVWAPRICE", Double),
    CLOSEPRICE("CLOSEPRICE", Double),
    MARKETPRICETODAY("MARKETPRICETODAY", Double),
    MARKETPRICE("MARKETPRICE", Double),
    LASTTOPREVPRICE("LASTTOPREVPRICE", Double),
    NUMTRADES("NUMTRADES", Int), //int 32
    VOLTODAY("VOLTODAY", Int), //int64
    VALTODAY("VALTODAY", Int), //int64
    VALTODAY_USD("VALTODAY_USD", Int), //int64
    ETFSETTLEPRICE("ETFSETTLEPRICE", Double),
    TRADINGSTATUS("TRADINGSTATUS", String),
    UPDATETIME("UPDATETIME", String), //time
    ADMITTEDQUOTE("ADMITTEDQUOTE", Double),
    LASTBID("LASTBID", String), //was undefined
    LASTOFFER("LASTOFFER", String), //was undefined
    LCLOSEPRICE("LCLOSEPRICE", Double),
    LCURRENTPRICE("LCURRENTPRICE", Double),
    MARKETPRICE2("MARKETPRICE2", Double),
    NUMBIDS("NUMBIDS", String), //was undefined
    NUMOFFERS("NUMOFFERS", String), //was undefined
    CHANGE("CHANGE", Double),
    TIME("TIME", String), //time
    HIGHBID("HIGHBID", String), //was undefined
    LOWOFFER("LOWOFFER", String), //was undefined
    PRICEMINUSPREVWAPRICE("PRICEMINUSPREVWAPRICE", Double),
    OPENPERIODPRICE("OPENPERIODPRICE", Double),
    SEQNUM("SEQNUM", Int), //int64
    SYSTIME("SYSTIME", String),   //dateString  //time
    CLOSINGAUCTIONPRICE("CLOSINGAUCTIONPRICE", Double),
    CLOSINGAUCTIONVOLUME("CLOSINGAUCTIONVOLUME", Double),
    ISSUECAPITALIZATION("ISSUECAPITALIZATION", Double),
    ISSUECAPITALIZATION_UPDATETIME("ISSUECAPITALIZATION_UPDATETIME", String), //time
    ETFSETTLECURRENCY("ETFSETTLECURRENCY", String),
    VALTODAY_RUR("VALTODAY_RUR", Int), //int64
    TRADINGSESSION("TRADINGSESSION", String),
}*/
