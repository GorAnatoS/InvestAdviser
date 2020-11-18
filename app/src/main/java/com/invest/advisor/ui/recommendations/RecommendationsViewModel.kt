package com.invest.advisor.ui.recommendations

import androidx.lifecycle.ViewModel
import com.invest.advisor.data.network.MoexNetworkDataSource
import com.invest.advisor.data.network.YahooNetworkDataSource
import com.invest.advisor.data.network.moexResponse.MarketDataResponse
import com.invest.advisor.data.network.moexResponse.SecuritiesResponse
import com.invest.advisor.data.network.yahooResponse.YahooResponse
import com.invest.advisor.internal.lazyDeferred

class RecommendationsViewModel(
    private val yahooNetworkDataSource: YahooNetworkDataSource,
    private val moexNetworkDataSource: MoexNetworkDataSource
) : ViewModel() {

    lateinit var yahooResponse: YahooResponse

    lateinit var marketDataResponse: MarketDataResponse
    lateinit var securitiesResponse: SecuritiesResponse

    val downloadedYahooData by lazyDeferred {
        yahooNetworkDataSource.fetchYahooData("CSCO")
    }

    val securities by lazyDeferred {
        moexNetworkDataSource.fetchSecurities()
    }
    val marketData by lazyDeferred {
        moexNetworkDataSource.fetchMarketData()
    }
}