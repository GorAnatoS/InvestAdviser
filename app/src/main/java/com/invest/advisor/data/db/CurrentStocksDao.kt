package com.invest.advisor.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.invest.advisor.data.db.entity.Securities

@Dao
interface CurrentStocksSecIdDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun upsertSecu(moexdata: MOEXdata)

    @Query("SELECT * FROM MOEX_data")
    fun getRoomSecurities(): LiveData<List<Securities>?>

    ////TODO 2020/07/28 20:05 || get prepared moex data
}