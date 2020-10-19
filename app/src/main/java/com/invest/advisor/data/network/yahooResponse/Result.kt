package com.invest.advisor.data.network.yahooResponse


import com.google.gson.annotations.SerializedName

data class Result(
    val assetProfile: AssetProfile,
    val financialData: FinancialData
)