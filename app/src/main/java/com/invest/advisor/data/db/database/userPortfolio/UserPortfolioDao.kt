package com.invest.advisor.data.db.database.userPortfolio

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * DAO for work with UserPortfolio Database
 */

@Dao
interface UserPortfolioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userPortfolioEntry: UserPortfolioEntry)

    @Update
    fun update(userPortfolioEntry: UserPortfolioEntry)

    @Query("SELECT * FROM $USER_PORTFOLIO_TABLE_NAME")
    fun getAllData(): LiveData<List<UserPortfolioEntry>>

    @Delete
    fun delete(userPortfolioEntry: UserPortfolioEntry)
}