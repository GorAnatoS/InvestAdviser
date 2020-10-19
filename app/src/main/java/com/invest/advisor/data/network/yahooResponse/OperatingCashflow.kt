package com.invest.advisor.data.network.yahooResponse


import com.google.gson.annotations.SerializedName

data class OperatingCashflow(
    val raw: Long,
    val fmt: String,
    val longFmt: String
)