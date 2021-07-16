package com.invest.advisor.data.db.entity.yahoo


data class FinancialData(
    val maxAge: Int,
    val currentPrice: com.invest.advisor.data.db.entity.yahoo.CurrentPrice,
    val targetHighPrice: com.invest.advisor.data.db.entity.yahoo.TargetHighPrice,
    val targetLowPrice: com.invest.advisor.data.db.entity.yahoo.TargetLowPrice,
    val targetMeanPrice: com.invest.advisor.data.db.entity.yahoo.TargetMeanPrice,
    val targetMedianPrice: com.invest.advisor.data.db.entity.yahoo.TargetMedianPrice,
    val recommendationMean: com.invest.advisor.data.db.entity.yahoo.RecommendationMean,
    val recommendationKey: String,
    val numberOfAnalystOpinions: com.invest.advisor.data.db.entity.yahoo.NumberOfAnalystOpinions,
    val totalCash: com.invest.advisor.data.db.entity.yahoo.TotalCash,
    val totalCashPerShare: com.invest.advisor.data.db.entity.yahoo.TotalCashPerShare,
    val ebitda: com.invest.advisor.data.db.entity.yahoo.Ebitda,
    val totalDebt: com.invest.advisor.data.db.entity.yahoo.TotalDebt,
    val quickRatio: com.invest.advisor.data.db.entity.yahoo.QuickRatio,
    val currentRatio: com.invest.advisor.data.db.entity.yahoo.CurrentRatio,
    val totalRevenue: com.invest.advisor.data.db.entity.yahoo.TotalRevenue,
    val debtToEquity: com.invest.advisor.data.db.entity.yahoo.DebtToEquity,
    val revenuePerShare: com.invest.advisor.data.db.entity.yahoo.RevenuePerShare,
    val returnOnAssets: com.invest.advisor.data.db.entity.yahoo.ReturnOnAssets,
    val returnOnEquity: com.invest.advisor.data.db.entity.yahoo.ReturnOnEquity,
    val grossProfits: com.invest.advisor.data.db.entity.yahoo.GrossProfits,
    val freeCashflow: com.invest.advisor.data.db.entity.yahoo.FreeCashflow,
    val operatingCashflow: com.invest.advisor.data.db.entity.yahoo.OperatingCashflow,
    val earningsGrowth: com.invest.advisor.data.db.entity.yahoo.EarningsGrowth,
    val revenueGrowth: com.invest.advisor.data.db.entity.yahoo.RevenueGrowth,
    val grossMargins: com.invest.advisor.data.db.entity.yahoo.GrossMargins,
    val ebitdaMargins: com.invest.advisor.data.db.entity.yahoo.EbitdaMargins,
    val operatingMargins: com.invest.advisor.data.db.entity.yahoo.OperatingMargins,
    val profitMargins: com.invest.advisor.data.db.entity.yahoo.ProfitMargins,
    val financialCurrency: String
)