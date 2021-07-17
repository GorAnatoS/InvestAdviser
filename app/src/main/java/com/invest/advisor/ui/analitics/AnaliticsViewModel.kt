package com.invest.advisor.ui.analitics

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.invest.advisor.data.db.database.userPortfolio.UserPortfolioDatabase
import com.invest.advisor.data.db.database.userPortfolio.UserPortfolioEntry
import com.invest.advisor.data.repository.UserPortfolioRepository

/**
 * ViewModel for [AnaliticsFragment]
 */

class AnaliticsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserPortfolioRepository

    var allData: LiveData<List<UserPortfolioEntry>>

    init {
        val userPortfolioDao = UserPortfolioDatabase.getInstance(application).userPortfolioDao
        repository = UserPortfolioRepository(userPortfolioDao)
        allData = repository.allData
    }
}