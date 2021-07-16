package com.invest.advisor.data.repository

import androidx.lifecycle.LiveData
import com.invest.advisor.data.db.database.userPortfolio.UserPortfolioDao
import com.invest.advisor.data.db.database.userPortfolio.UserPortfolioEntry

/**
 * Created by qsufff on 9/13/2020.
 */

class UserPortfolioRepository(private val userPortfolioDao: UserPortfolioDao) {

    var allData: LiveData<List<UserPortfolioEntry>> = userPortfolioDao.getAllData()

    suspend fun insert(entry: UserPortfolioEntry) {
        userPortfolioDao.insert(entry)
    }

    suspend fun delete(entry: UserPortfolioEntry) {
        userPortfolioDao.delete(entry)
    }
}