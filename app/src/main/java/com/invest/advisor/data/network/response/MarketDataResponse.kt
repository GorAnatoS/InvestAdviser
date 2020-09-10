package com.invest.advisor.data.network.response

import com.google.gson.annotations.SerializedName
import com.invest.advisor.data.db.entity.MarketData



/**
 * Created by qsufff on 7/29/2020.
 */

data class MarketDataResponse(
    @SerializedName("marketdata")
    val currentMarketData: MarketData
)