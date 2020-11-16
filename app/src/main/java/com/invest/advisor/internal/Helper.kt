package com.invest.advisor.internal

import java.math.RoundingMode
import java.text.DecimalFormat


/**
 * Created by qsufff on 11/16/2020.
 */

class Helper {
    companion object{
        fun roundOffDecimal(number: Double): Double? {
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING
            return df.format(number).toDouble()
        }
    }

}