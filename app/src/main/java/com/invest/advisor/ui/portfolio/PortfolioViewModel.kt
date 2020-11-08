package com.invest.advisor.ui.portfolio

import android.app.Application
import androidx.lifecycle.*
import com.invest.advisor.data.db.userPortfolio.UserPortfolio
import com.invest.advisor.data.db.userPortfolio.UserPortfolioEntry
import com.invest.advisor.data.repository.UserPortfolioRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PortfolioViewModel(application: Application) : AndroidViewModel(application) {

    val numOfShares: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    private val repository: UserPortfolioRepository

    var allData: LiveData<List<UserPortfolioEntry>>

    init {
        val userPortfolioDao = UserPortfolio.getInstance(application).userPortfolioDao
        repository = UserPortfolioRepository(userPortfolioDao)
        allData = repository.allData
    }

    fun insert(userPortfolioEntry: UserPortfolioEntry) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(userPortfolioEntry)
    }


    fun delete(userPortfolioEntry: UserPortfolioEntry) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(userPortfolioEntry)
        //allData = repository.allData
    }
}