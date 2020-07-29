package com.invest.advisor.data.network.response

import com.google.gson.annotations.SerializedName
import com.invest.advisor.data.db.entity.Securities
import com.invest.advisor.data.db.secList.SecEntry


/**
 * Created by qsufff on 7/29/2020.
 */

data class SecuritiesResponse(
    @SerializedName("securities")
    val currentSecurities: Securities
)