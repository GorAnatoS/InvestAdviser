package com.invest.advisor.data.network.yahooResponse


import com.google.gson.annotations.SerializedName

data class EbitdaMargins(
    val raw: Double,
    val fmt: String
)