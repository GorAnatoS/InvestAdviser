package com.invest.advisor.data.network.yahooResponse


data class AssetProfile(
    val address1: String,
    val city: String,
    val zip: String,
    val country: String,
    val phone: String,
    val website: String,
    val industry: String,
    val sector: String,
    val longBusinessSummary: String,
    val fullTimeEmployees: Int,
    val companyOfficers: List<CompanyOfficer>,
    val auditRisk: Int,
    val boardRisk: Int,
    val compensationRisk: Int,
    val shareHolderRightsRisk: Int,
    val overallRisk: Int,
    val governanceEpochDate: Int,
    val maxAge: Int
)