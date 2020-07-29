package com.invest.advisor.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.invest.advisor.data.network.response.CurrentSecuritiesResponse
import com.invest.advisor.data.network.response.IssApiService
import com.invest.advisor.internal.NoConnectivityException

class SecuritiesNetworkDataSourceImpl(
    private val issApiService: IssApiService
) : SecuritiesNetworkDataSource {


    private val _downloadedCurrentSecurities = MutableLiveData<CurrentSecuritiesResponse>()
    override val downloadedCurrentSecurities: LiveData<CurrentSecuritiesResponse>
        get() = _downloadedCurrentSecurities

    override suspend fun downloadCurrentSecurities() {
       try {
           issApiService.getSecuritiesListAsync().await()
           _downloadedCurrentSecurities.postValue(issApiService.getSecuritiesListAsync().await())
       } catch (e: NoConnectivityException) {
           Log.e("Connectivity", "No internet connection.", e)
       }
    }
}