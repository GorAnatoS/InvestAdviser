package com.invest.advisor.data.network.yahooResponse


data class QuoteSummary(
    val result: List<Result>,
    val error: Any?
)