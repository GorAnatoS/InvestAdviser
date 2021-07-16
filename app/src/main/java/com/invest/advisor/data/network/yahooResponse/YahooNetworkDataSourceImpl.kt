package com.invest.advisor.data.network.yahooResponse

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.invest.advisor.data.network.yahooResponse.YahooApiService
import com.invest.advisor.data.network.yahooResponse.YahooNetworkDataSource
import com.invest.advisor.data.network.yahooResponse.YahooResponse
import com.invest.advisor.internal.NoConnectivityException

/*
*  YahooNetworkDataSource implementation for Yahoo API
*/

class YahooNetworkDataSourceImpl(
    private val yahooApiService: YahooApiService
) : YahooNetworkDataSource {

    private val _downloadedYahooResponse = MutableLiveData<YahooResponse>()
    override val downloadedYahooResponse: LiveData<YahooResponse>
        get() = _downloadedYahooResponse

    //fetch assetName from Yahoo.com
    override suspend fun fetchYahooData(assetName: String) {
        try {
            yahooApiService.getAssetProfileAsync(assetName).await()
            _downloadedYahooResponse.postValue(yahooApiService.getAssetProfileAsync(assetName).await())
        } catch (e: Resources.NotFoundException) {
            Log.e("Fetch data", "Not found 404.", e)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
        }
    }
}