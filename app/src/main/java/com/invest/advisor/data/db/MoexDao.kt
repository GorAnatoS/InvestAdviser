package com.invest.advisor.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.invest.advisor.data.db.entity.EnumMarketData
import com.invest.advisor.data.db.entity.MarketData
import com.invest.advisor.data.db.entity.Securities
import com.invest.advisor.data.db.secList.SecEntry
import com.invest.advisor.data.db.secList.SecListEntry
import com.invest.advisor.data.network.response.SecuritiesResponse

@Dao
interface SecuritiesDao {
/*    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(secEntry: SecEntry)*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(securities: Securities)


    @Query("SELECT * FROM securities_data")
    fun getRoomSecurities(): LiveData<List<Securities>>
}

@Dao
interface MarketDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(marketData: MarketData)

    @Query("SELECT * FROM market_data")
    fun getRoomMarketData(): LiveData<List<MarketData>>
}