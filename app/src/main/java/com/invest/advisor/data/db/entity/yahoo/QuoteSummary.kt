package com.invest.advisor.data.db.entity.yahoo


data class QuoteSummary(
    val result: List<com.invest.advisor.data.db.entity.yahoo.Result>,
    val error: Any?
)