package com.invest.advisor.data.network

import androidx.lifecycle.LiveData
import com.invest.advisor.data.network.response.SecuritiesResponse


/**
 * Created by qsufff on 7/29/2020.
 */

interface SecuritiesNetworkDataSource {
    val downloadedSecurities: LiveData<SecuritiesResponse>

//    suspend fun fetchCurrentSecurities(
//
//    )

    suspend fun fetchSecurities()
}