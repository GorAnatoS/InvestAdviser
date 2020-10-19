package com.invest.advisor.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.invest.advisor.data.db.entity.MarketData
import com.invest.advisor.data.db.entity.Securities

@Dao
interface MoexDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(securities: Securities)

    @Query("SELECT * FROM securities_data")
    fun getRoomSecurities(): LiveData<List<Securities>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(marketData: MarketData)

    @Query("SELECT * FROM market_data")
    fun getRoomMarketData(): LiveData<List<MarketData>>
}
