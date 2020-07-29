package com.invest.advisor.data.network

import androidx.lifecycle.LiveData
import com.invest.advisor.data.network.response.CurrentSecuritiesResponse


/**
 * Created by qsufff on 7/29/2020.
 */

interface SecuritiesNetworkDataSource {
    val downloadedCurrentSecurities: LiveData<CurrentSecuritiesResponse>

//    suspend fun fetchCurrentSecurities(
//
//    )

    suspend fun downloadCurrentSecurities()
}