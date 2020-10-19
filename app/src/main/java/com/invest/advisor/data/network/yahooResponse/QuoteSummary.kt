package com.invest.advisor.data.network.yahooResponse


import com.google.gson.annotations.SerializedName

data class QuoteSummary(
    val result: List<Result>,
    val error: Any?
)