package com.example.app.data

import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

data class Day(
    val date: org.threeten.bp.LocalDate,
    val isSelected: Boolean = false
) {
    fun getFormattedDate(): String =
        date.format(org.threeten.bp.format.DateTimeFormatter.ofPattern("d"))

    fun getDayOfWeek(): String =
        date.format(DateTimeFormatter.ofPattern("E", Locale("ru")))
}