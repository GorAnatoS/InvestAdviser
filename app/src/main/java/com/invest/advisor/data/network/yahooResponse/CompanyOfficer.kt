package com.invest.advisor.data.network.yahooResponse


data class CompanyOfficer(
    val maxAge: Int,
    val name: String,
    val age: Int,
    val title: String,
    val yearBorn: Int,
    val exercisedValue: ExercisedValue,
    val unexercisedValue: UnexercisedValue
)