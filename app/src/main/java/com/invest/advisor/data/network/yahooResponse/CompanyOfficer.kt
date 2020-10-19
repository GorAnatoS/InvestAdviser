package com.invest.advisor.data.network.yahooResponse


import com.google.gson.annotations.SerializedName

data class CompanyOfficer(
    val maxAge: Int,
    val name: String,
    val age: Int,
    val title: String,
    val yearBorn: Int,
    val fiscalYear: Int,
    val totalPay: TotalPay,
    val exercisedValue: ExercisedValue,
    val unexercisedValue: UnexercisedValue
)