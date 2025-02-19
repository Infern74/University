package com.example.app.utils

import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.temporal.TemporalAdjusters

object DateUtils {
    // Получить список дней текущей недели (с понедельника по воскресенье)
    fun getCurrentWeekDays(): List<LocalDate> {
        val today = LocalDate.now()
        val firstDayOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        return (0 until 7).map { firstDayOfWeek.plusDays(it.toLong()) }
    }

    // Проверка, является ли дата сегодняшним днем
    fun isToday(date: LocalDate): Boolean {
        return date == LocalDate.now()
    }
}