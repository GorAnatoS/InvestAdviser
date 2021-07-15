package com.invest.advisor.data.db.entity

//entry for MoexFragment with list of MOEX entries

data class MoexEntry(
    val secId: String?,
    var secName: String?,
    val secPrice: String?,
    val secChange: String?,
    val secChangePcnt: String?
)
