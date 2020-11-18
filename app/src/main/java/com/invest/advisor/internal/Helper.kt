package com.invest.advisor.internal

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*


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

        fun formatedDateStringToFormatedDateLong(string: String): Long {
            val date = Calendar.getInstance().time
            val formatter =
                SimpleDateFormat.getDateInstance() //or use getDateInstance()
            var formatedDateString = formatter.format(date)
            var formatedDateLong = formatter.parse(formatedDateString).time
            return formatedDateLong
        }

        fun getFormatedDateString(): String {
            val date = Calendar.getInstance().time
            val formatter =
                SimpleDateFormat.getDateInstance()
            var formatedDateString = formatter.format(date)
            return formatedDateString
        }

        fun getFormatedDateString(ms: Long): String {
            val date = Calendar.getInstance().time
            val formatter =
                SimpleDateFormat.getDateInstance()
            var formatedDateString = formatter.format(ms)

            return formatedDateString
        }
    }

}