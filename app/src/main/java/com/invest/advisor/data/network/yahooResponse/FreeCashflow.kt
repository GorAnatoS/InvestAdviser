package com.invest.advisor.data.network.yahooResponse


import com.google.gson.annotations.SerializedName

data class FreeCashflow(
    val raw: Double,
    val fmt: String,
    val longFmt: String
)