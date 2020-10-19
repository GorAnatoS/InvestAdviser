package com.invest.advisor.data.network.yahooResponse


import com.google.gson.annotations.SerializedName

data class RegularMarketDayLow(
    val raw: Double,
    val fmt: String
)