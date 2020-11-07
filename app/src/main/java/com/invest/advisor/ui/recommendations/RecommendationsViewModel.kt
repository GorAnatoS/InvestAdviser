package com.invest.advisor.ui.analitics

import androidx.lifecycle.ViewModel
import com.invest.advisor.data.network.YahooNetworkDataSource
import com.invest.advisor.data.network.moexResponse.MarketDataResponse
import com.invest.advisor.data.network.moexResponse.SecuritiesResponse
import com.invest.advisor.data.network.yahooResponse.YahooResponse
import com.invest.advisor.internal.lazyDeferred

class RecommendationsViewModel(
    private val yahooNetworkDataSource: YahooNetworkDataSource
) : ViewModel() {

    lateinit var yahooResponse: YahooResponse

    val downloadedYahooData by lazyDeferred {
        yahooNetworkDataSource.fetchYahooData("CSCO")
    }
}