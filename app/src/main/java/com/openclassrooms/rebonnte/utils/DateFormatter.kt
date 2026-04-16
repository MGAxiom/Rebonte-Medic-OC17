package com.openclassrooms.rebonnte.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormatter {
    private const val PATTERN = "EEE d MMM yyyy - HH'h'mm"

    fun getCurrentFormattedDate(): String {
        val formatter = SimpleDateFormat(PATTERN, Locale.ENGLISH)
        return formatter.format(Date())
    }
}
