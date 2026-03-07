package com.example.weatherapp.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getFormattedDate(dt: Int, pattern: String = "d MMMM yyyy"): String {
    return SimpleDateFormat(pattern, Locale.forLanguageTag("pl-PL")).format(Date(dt.toLong() * 1000))
}
