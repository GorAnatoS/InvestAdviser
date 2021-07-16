package com.invest.advisor.data.db.entity.yahoo


data class AssetProfile(
    val address1: String,
    val address2: String,
    val city: String,
    val zip: String,
    val country: String,
    val phone: String,
    val website: String,
    val industry: String?,
    val sector: String,
    val longBusinessSummary: String,
    val companyOfficers: List<com.invest.advisor.data.db.entity.yahoo.CompanyOfficer>,
    val auditRisk: Int,
    val boardRisk: Int,
    val compensationRisk: Int,
    val shareHolderRightsRisk: Int,
    val overallRisk: Int,
    val governanceEpochDate: Int,
    val maxAge: Int
)