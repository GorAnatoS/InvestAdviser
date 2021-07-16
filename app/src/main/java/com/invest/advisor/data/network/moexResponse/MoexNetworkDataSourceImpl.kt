package com.invest.advisor.data.network.moexResponse

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.invest.advisor.data.network.moexResponse.MarketDataResponse
import com.invest.advisor.data.network.moexResponse.MoexApiService
import com.invest.advisor.data.network.moexResponse.MoexNetworkDataSource
import com.invest.advisor.data.network.moexResponse.SecuritiesResponse
import com.invest.advisor.internal.NoConnectivityException

/*
*  MoexNetworkDataSource implementation for MOEX API
*/

class MoexNetworkDataSourceImpl(
    private val moexApiService: MoexApiService
) : MoexNetworkDataSource {
    //MOEX securities
    private val _downloadedCurrentSecurities = MutableLiveData<SecuritiesResponse>()
    override val downloadedSecurities: LiveData<SecuritiesResponse>
        get() = _downloadedCurrentSecurities

    //MOEX market data
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