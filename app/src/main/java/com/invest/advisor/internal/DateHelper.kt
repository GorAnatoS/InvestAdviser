package com.invest.advisor.internal

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


/**
 * Helper for working with dates
 */

class DateHelper {
    companion object {
        fun roundOffDecimal(number: Double): Double? {
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING
            return df.format(number).toDouble()
        }

        fun formattedDateStringToFormattedDateLong(string: String): Long {
            val date = Calendar.getInstance().time
            val formatter =
                SimpleDateFormat.getDateInstance() //or use getDateInstance()
            val formattedDateString = formatter.format(date)

            return formatter.parse(formattedDateString).time
        }

        fun getFormattedDateString(): String {
            val date = Calendar.getInstance().time
            val formatter =
                SimpleDateFormat.getDateInstance()
            return formatter.format(date)
        }

        fun getFormattedDateString(ms: Long): String {
            val formatter =
                SimpleDateFormat.getDateInstance()
            return formatter.format(ms)
        }
    }
}