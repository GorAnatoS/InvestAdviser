package com.invest.advisor.ui.moex

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.invest.advisor.data.repository.MoexRepository
import com.invest.advisor.internal.lazyDeffered

class MoexViewModel(
    private val moexRepository: MoexRepository

) : ViewModel() {

    val securities by lazyDeffered {
        moexRepository.getSecurities()
    }
    val marketData by lazyDeffered {
        moexRepository.getMarketData()
    }

}