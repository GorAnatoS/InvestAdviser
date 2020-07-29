package com.invest.advisor.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.invest.advisor.data.network.response.MarketDataResponse
import com.invest.advisor.data.network.response.SecuritiesResponse
import com.invest.advisor.data.network.response.MoexApiService
import com.invest.advisor.internal.NoConnectivityException

class MoexNetworkDataSourceImpl(
    private val moexApiService: MoexApiService
) : MoexNetworkDataSource {


    private val _downloadedCurrentSecurities = MutableLiveData<SecuritiesResponse>()
    override val downloadedSecurities: LiveData<SecuritiesResponse>
        get() = _downloadedCurrentSecurities

    private val _downloadedMarketData = MutableLiveData<MarketDataResponse>()
    override val downloadedMarketData: LiveData<MarketDataResponse>
        get() = _downloadedMarketData

    override suspend fun fetchSecurities() {
       try {
           moexApiService.getSecuritiesAsync().await()
           _downloadedCurrentSecurities.postValue(moexApiService.getSecuritiesAsync().await())
       } catch (e: NoConnectivityException) {
           Log.e("Connectivity", "No internet connection.", e)
       }
    }

    override suspend fun fetchMarketData() {
        try {
            moexApiService.getMarketDataAsync().await()
            _downloadedMarketData.postValue(moexApiService.getMarketDataAsync().await())
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
        }
    }
}