package com.invest.advisor.data.network.yahooResponse


data class Result(
    val assetProfile: AssetProfile,
    val price: Price,
    val financialData: FinancialData
)