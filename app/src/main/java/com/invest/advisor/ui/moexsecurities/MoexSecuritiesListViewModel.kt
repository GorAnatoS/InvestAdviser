package com.invest.advisor.ui.moexsecurities

import androidx.lifecycle.ViewModel
import com.invest.advisor.data.network.moexResponse.MoexNetworkDataSource
import com.invest.advisor.data.network.moexResponse.MarketDataResponse
import com.invest.advisor.data.network.moexResponse.SecuritiesResponse
import com.invest.advisor.internal.lazyDeferred

class MoexSecuritiesListViewModel(
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