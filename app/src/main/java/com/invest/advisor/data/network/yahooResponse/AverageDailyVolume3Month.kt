package com.invest.advisor.data.network.yahooResponse


import com.google.gson.annotations.SerializedName

class AverageDailyVolume3Month(
    val raw: Double,
    val fmt: String,
    val longFmt: String
)