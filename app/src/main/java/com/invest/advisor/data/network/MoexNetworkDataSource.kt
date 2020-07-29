package com.invest.advisor.data.network

import androidx.lifecycle.LiveData
import com.invest.advisor.data.network.response.MarketDataResponse
import com.invest.advisor.data.network.response.SecuritiesResponse


/**
 * Created by qsufff on 7/29/2020.
 */

interface MoexNetworkDataSource {
    val downloadedSecurities: LiveData<SecuritiesResponse>
    val downloadedMarketData: LiveData<MarketDataResponse>

    suspend fun fetchSecurities()

    suspend fun fetchMarketData()
}