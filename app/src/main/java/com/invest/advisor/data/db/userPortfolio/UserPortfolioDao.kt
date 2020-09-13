package com.invest.advisor.data.db.userPortfolio

import androidx.lifecycle.LiveData
import androidx.room.*


/**
 * Created by qsufff on 9/13/2020.
 */
@Dao
interface UserPortfolioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userPortfolioEntry: UserPortfolioEntry)

    @Update
    fun update(userPortfolioEntry: UserPortfolioEntry)

    @Query("SELECT * FROM $TABLE_NAME")
    fun getAllData(): LiveData<List<UserPortfolioEntry>>

}