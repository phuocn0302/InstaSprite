package com.olaz.instasprite.utils

import java.text.DateFormat
import java.util.Locale

fun Long.toDateString(dateFormat: Int =  DateFormat.MEDIUM): String {
    val df = DateFormat.getDateInstance(dateFormat, Locale.getDefault())
    return df.format(this)
}