package com.invest.advisor.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.invest.advisor.data.network.response.SecuritiesResponse
import com.invest.advisor.data.network.response.MoexApiService
import com.invest.advisor.internal.NoConnectivityException

class SecuritiesNetworkDataSourceImpl(
    private val moexApiService: MoexApiService
) : SecuritiesNetworkDataSource {


    private val _downloadedCurrentSecurities = MutableLiveData<SecuritiesResponse>()
    override val downloadedSecurities: LiveData<SecuritiesResponse>
        get() = _downloadedCurrentSecurities

    override suspend fun fetchSecurities() {
       try {
           moexApiService.getSecuritiesListAsync().await()
           _downloadedCurrentSecurities.postValue(moexApiService.getSecuritiesListAsync().await())
       } catch (e: NoConnectivityException) {
           Log.e("Connectivity", "No internet connection.", e)
       }
    }
}