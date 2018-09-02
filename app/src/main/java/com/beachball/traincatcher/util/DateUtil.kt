package com.beachball.traincatcher.util

import java.text.SimpleDateFormat
import java.util.*

class DateUtil() {

    companion object {
        private const val DATE_FORMAT = "yyyy-MM-dd'T'hh:mm:ss'Z'"
        private val formatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())

        fun convertToCalendar(dateStr: String): Calendar {
            val date = formatter.parse(dateStr)
            val cal = Calendar.getInstance()
            cal.time = date
            return cal
        }
    }
}