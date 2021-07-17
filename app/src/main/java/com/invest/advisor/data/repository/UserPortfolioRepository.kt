package com.invest.advisor.data.repository

import androidx.lifecycle.LiveData
import com.invest.advisor.data.db.database.userPortfolio.UserPortfolioDao
import com.invest.advisor.data.db.database.userPortfolio.UserPortfolioEntry

/**
 * Repository for
 *
 */

class UserPortfolioRepository(private val userPortfolioDao: UserPortfolioDao) {

    var allData: LiveData<List<UserPortfolioEntry>> = userPortfolioDao.getAllData()

    fun insert(entry: UserPortfolioEntry) {
        userPortfolioDao.insert(entry)
    }

    fun delete(entry: UserPortfolioEntry) {
        userPortfolioDao.delete(entry)
    }
}