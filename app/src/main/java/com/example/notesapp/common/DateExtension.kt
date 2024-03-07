package com.example.notesapp.common

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

private const val DATE_FORMAT = "dd MMM yyyy HH:mm:ss"
private const val TIME_ZONE = "Europe/Istanbul"
fun getCurrentTime(): String = SimpleDateFormat(DATE_FORMAT, Locale("tr", "TR")).run {
    timeZone = TimeZone.getTimeZone(TIME_ZONE)
    format(Date())
}