package com.invest.advisor.data.db.entity.yahoo


data class Result(
    val assetProfile: com.invest.advisor.data.db.entity.yahoo.AssetProfile,
    val price: com.invest.advisor.data.db.entity.yahoo.Price,
    val financialData: com.invest.advisor.data.db.entity.yahoo.FinancialData
)