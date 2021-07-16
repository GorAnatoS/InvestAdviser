package com.invest.advisor.data.network.moexResponse

import androidx.lifecycle.LiveData
import com.invest.advisor.data.network.moexResponse.MarketDataResponse
import com.invest.advisor.data.network.moexResponse.SecuritiesResponse


/**
 * MoexNetworkDataSource interface
 */

interface MoexNetworkDataSource {
    val downloadedSecurities: LiveData<SecuritiesResponse>
    val downloadedMarketData: LiveData<MarketDataResponse>

    suspend fun fetchSecurities()
    suspend fun fetchMarketData()
}