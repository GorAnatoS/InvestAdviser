package com.invest.advisor.data.db.entity

import com.invest.advisor.ui.moexsecurities.items.MoexSecurityItem

/**
 * Entry for [MoexSecurityItem] with only necessary data
 */

data class MoexSecurityEntry(
    val secId: String?,
    var secName: String?,
    val secPrice: String?,
    val secChange: String?,
    val secChangePcnt: String?
)