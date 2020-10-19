package com.invest.advisor.data.network.yahooResponse


import com.google.gson.annotations.SerializedName

data class FinancialData(
    val maxAge: Int,
    val currentPrice: CurrentPrice,
    val targetHighPrice: TargetHighPrice,
    val targetLowPrice: TargetLowPrice,
    val targetMeanPrice: TargetMeanPrice,
    val targetMedianPrice: TargetMedianPrice,
    val recommendationMean: RecommendationMean,
    val recommendationKey: String,
    val numberOfAnalystOpinions: NumberOfAnalystOpinions,
    val totalCash: TotalCash,
    val totalCashPerShare: TotalCashPerShare,
    val ebitda: Ebitda,
    val totalDebt: TotalDebt,
    val quickRatio: QuickRatio,
    val currentRatio: CurrentRatio,
    val totalRevenue: TotalRevenue,
    val debtToEquity: DebtToEquity,
    val revenuePerShare: RevenuePerShare,
    val returnOnAssets: ReturnOnAssets,
    val returnOnEquity: ReturnOnEquity,
    val grossProfits: GrossProfits,
    val freeCashflow: FreeCashflow,
    val operatingCashflow: OperatingCashflow,
    val earningsGrowth: EarningsGrowth,
    val revenueGrowth: RevenueGrowth,
    val grossMargins: GrossMargins,
    val ebitdaMargins: EbitdaMargins,
    val operatingMargins: OperatingMargins,
    val profitMargins: ProfitMargins,
    val financialCurrency: String
)