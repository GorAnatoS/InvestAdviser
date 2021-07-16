package com.invest.advisor.internal

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 * Class for helping with math operations.
 */

class MathHelper {
    companion object {
        fun roundOffDecimal(number: Double): Double? {
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING
            return df.format(number).toDouble()
        }
    }
}