package com.invest.advisor.data.network.yahooResponse

import androidx.lifecycle.LiveData
import com.invest.advisor.data.network.yahooResponse.YahooResponse


/**
 *
 */

interface YahooNetworkDataSource {
    val downloadedYahooResponse: LiveData<YahooResponse>
    suspend fun fetchYahooData(assetName: String)
}