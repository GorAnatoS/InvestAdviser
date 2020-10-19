package com.invest.advisor.data.network.yahooResponse


data class CompanyOfficer(
    val maxAge: Int,
    val name: String,
    val title: String,
    val exercisedValue: ExercisedValue,
    val unexercisedValue: UnexercisedValue,
    val age: Int,
    val yearBorn: Int
)