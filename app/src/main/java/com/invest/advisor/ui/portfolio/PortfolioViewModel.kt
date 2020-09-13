package com.invest.advisor.ui.portfolio

import android.app.Application
import androidx.lifecycle.*
import com.invest.advisor.data.db.userPortfolio.UserPortfolio
import com.invest.advisor.data.db.userPortfolio.UserPortfolioEntry
import com.invest.advisor.data.repository.UserPortfolioRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PortfolioViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserPortfolioRepository

    val allData: LiveData<List<UserPortfolioEntry>>

    init {
        val userPortfolioDao = UserPortfolio.getInstance(application).userPortfolioDao
        repository = UserPortfolioRepository(userPortfolioDao)
        allData = repository.allData


    }

    fun insert(userPortfolioEntry: UserPortfolioEntry) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(userPortfolioEntry)
    }
}