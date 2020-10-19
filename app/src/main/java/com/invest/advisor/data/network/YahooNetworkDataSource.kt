package com.invest.advisor.data.network

import androidx.lifecycle.LiveData
import com.invest.advisor.data.network.yahooResponse.AssetProfile
import com.invest.advisor.data.network.yahooResponse.YahooResponse


/**
 * Created by qsufff on 7/29/2020.
 */

interface YahooNetworkDataSource {
    val downloadedYahooResponse: LiveData<YahooResponse>

    suspend fun fetchYahooData(assetName: String)
}