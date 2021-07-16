package com.invest.advisor.data.db.entity.yahoo


data class CompanyOfficer(
    val maxAge: Int,
    val name: String,
    val title: String,
    val exercisedValue: com.invest.advisor.data.db.entity.yahoo.ExercisedValue,
    val unexercisedValue: com.invest.advisor.data.db.entity.yahoo.UnexercisedValue,
    val age: Int,
    val yearBorn: Int
)