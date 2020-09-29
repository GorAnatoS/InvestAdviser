package com.invest.advisor.ui.moex

import androidx.lifecycle.ViewModel
import com.invest.advisor.data.network.MoexNetworkDataSource
import com.invest.advisor.data.network.response.MarketDataResponse
import com.invest.advisor.data.network.response.SecuritiesResponse
import com.invest.advisor.data.repository.MoexRepository
import com.invest.advisor.internal.lazyDeferred

class MoexViewModel(
    private val moexNetworkDataSource: MoexNetworkDataSource
) : ViewModel() {

    lateinit var marketDataResponse: MarketDataResponse
    lateinit var securitiesResponse: SecuritiesResponse

    val securities by lazyDeferred {
        moexNetworkDataSource.fetchSecurities()
    }
    val marketData by lazyDeferred {
        moexNetworkDataSource.fetchMarketData()
    }
}